/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.AccountType;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.persistence.PostRepository;
import se.chalmers.bokforing.persistence.VerificationRepository;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.service.PostService;
import static se.chalmers.bokforing.util.Constants.REVENUE_ACCOUNTS;

/**
 *
 * @author Jakob
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private VerificationRepository verRepo;

    @Autowired
    private AccountService accountService;

    @Override
    public Map<Account, List<Post>> getGeneralLedger(UserAccount user) {
        Map<Account, List<Post>> generalLedger = new HashMap<>();

        List<Account> accounts = accountService.findAllAccounts();
        for (Account account : accounts) {
            Page<Post> posts = postRepo.findPostsForUserAndAccountAndActive(user.getId(), account.getNumber(), true, null);

            generalLedger.put(account, posts.getContent());
        }

        return generalLedger;
    }

    @Override
    public void save(Post post) {
        if (post != null) {
            postRepo.save(post);
        }
    }

    /**
     *
     * @param user
     * @param startDate
     * @param endDate
     * @param pageable
     * @return balanceSheet, a mapping from the accounts the user has used to a
     * list where the first value in the list is the sum of all posts with that
     * account number during that period and the second value is the opening
     * balance of that account. Other things needed to create the full
     * balanceSheet on the receivers end is title for the account types, company
     * name, period and so on (look in docs for specifications).
     */
    @Override
    public Map<Account, List<Double>> getBalanceSheet(UserAccount user, Date startDate,
            Date endDate, Pageable pageable) {

        Map<Account, List<Double>> balanceSheet = new HashMap<>();
        List<Verification> givenPeriodVerifications = verRepo.findByUserAccountAndTransactionDateBetween(user, startDate, endDate, pageable).getContent();

        for (Verification verification : givenPeriodVerifications) {
            List<Post> posts = verification.getPosts();
            for (Post post : posts) {
                if (post.isActive()) {
                    Account account = post.getAccount();
                    if (account.getNumber() >= REVENUE_ACCOUNTS) {
                        continue;
                    }
                    if (!balanceSheet.containsKey(account)) {
                        List<Double> balanceList = new ArrayList<>();
                        balanceList.add(post.getBalance());
                        balanceList.add(0.0);
                        balanceSheet.put(account, balanceList);
                    } else {
                        balanceSheet.get(account).set(0, balanceSheet.get(account).get(0) + post.getBalance());
                    }
                }
            }
        }

        
        Calendar cal = Calendar.getInstance();
        cal.set(0, 0, 0);
        
        Date earlyDate = cal.getTime();
        List<Verification> earlierVerifications = verRepo.findByUserAccountAndTransactionDateBetween(user, earlyDate, startDate, pageable).getContent();
        
        for (Verification verification : earlierVerifications) {
            List<Post> posts = verification.getPosts();
            for (Post post : posts) {
                if (post.isActive()) {
                    Account account = post.getAccount();
                    if (account.getNumber() >= REVENUE_ACCOUNTS) {
                        continue;
                    }
                    if (!balanceSheet.containsKey(account)) {
                        List<Double> balanceList = new ArrayList<>();
                        Double periodBalance = 0.0;
                        balanceList.add(periodBalance);
                        balanceList.add(post.getBalance());
                        balanceSheet.put(account, balanceList);
                    } else {
                        balanceSheet.get(account).set(1, balanceSheet.get(account).get(1) + post.getBalance());
                    }
                }
            }
        }
        return balanceSheet;
    }

    /**
     *
     * @param user
     * @param startDate
     * @param endDate
     * @param pageable
     * @return incomeStatement, a mapping from the accounts the user has used to
     * the sum of all posts during that period. Other things needed to create
     * the full incomeStatement on the receivers end is title for the account
     * types, company name, period and so on.
     */
    @Override
    public Map<Account, Double> getIncomeStatement(UserAccount user, Date startDate,
            Date endDate, Pageable pageable) {
        Map<Account, Double> incomeStatement = new TreeMap<>();
        List<Verification> verifications = verRepo.findByUserAccountAndCreationDateBetween(user, startDate, endDate, pageable).getContent();

        for (Verification verification : verifications) {
            List<Post> posts = verification.getPosts();
            for (Post post : posts) {
                if (post.isActive()) {
                    Account account = post.getAccount();
                    if (account.getNumber() < REVENUE_ACCOUNTS) {
                        continue;
                    }
                    if (!incomeStatement.containsKey(account)) {
                        incomeStatement.put(account, post.getBalance());
                    } else {
                        incomeStatement.put(account, incomeStatement.get(account) + post.getBalance());
                    }
                }
            }
        }

        return incomeStatement;
    }

    @Override
    public Page<Post> findPostsForUserAndAccount(UserAccount user, Account account, boolean isActive, PagingAndSortingTerms terms) {
        PageRequest request = terms.getPageRequest();

        return postRepo.findPostsForUserAndAccountAndActive(user.getId(), account.getNumber(), isActive, request);
    }

    @Override
    public Post findPostById(long id) {
        return postRepo.findOne(id);
    }

    @Override
    public double getBalanceForAccountTypeBetweenDates(UserAccount user, AccountType accountType, Date start, Date end) {
        int startingDigit = accountType.getStartingDigit();

        int startNumber = startingDigit * 1000;
        int endNumber = startNumber + 999;

        List<Post> posts = postRepo.findByVerification_UserAccountAndAccount_NumberBetweenAndVerification_TransactionDateBetween(user, startNumber, endNumber, start, end);

        double balance = 0;

        for (Post post : posts) {
            balance += post.getBalance();
        }

        return balance;
    }

    @Override
    public Map<Date, Double> getBalanceForAccountAtDate(UserAccount user, AccountType accountType, Date startDate, Date endDate) {
        Map<Date, Double> map = new HashMap<>();

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        while (!start.after(end)) {
            Date date = start.getTime();

            // Between the same date gives only one day
            double balanceForDate = getBalanceForAccountTypeBetweenDates(user, accountType, date, date);
            map.put(date, balanceForDate);

            start.add(Calendar.DATE, 1);
        }

        return map;
    }
}
