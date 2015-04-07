/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.persistence.PostRepository;
import se.chalmers.bokforing.persistence.VerificationRepository;
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
     * @return balanceSheet, a mapping from the accounts the user has used to
     * the sum of all posts during that period and the opening balance. Other
     * things needed to create the full balanceSheet on the receivers end is
     * title for the account types, company name, period and so on.
     */
    @Override
    public Map<Account, List<Double>> getBalanceSheet(UserAccount user, Date startDate,
            Date endDate, Pageable pageable) {
        Map<Account, List<Double>> balanceSheet = new HashMap<>();
        List<Verification> givenPeriodVerifications = verRepo.findByUserAccountAndCreationDateBetween(user, startDate, endDate, pageable).getContent();

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
                        balanceList.add(post.getPostSum().getSumTotal());
                        balanceSheet.put(account, balanceList);
                    } else {
                        balanceSheet.get(account).set(0, balanceSheet.get(account).get(0) + post.getPostSum().getSumTotal());
                    }
                }
            }
        }
        Date earlyDate = new Date(00, 00, 00);
        List<Verification> earlierVerifications = verRepo.findByUserAccountAndCreationDateBetween(user, earlyDate, startDate, pageable).getContent();

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
                        balanceList.add(post.getPostSum().getSumTotal());
                        balanceSheet.put(account, balanceList);
                    } else {
                        balanceSheet.get(account).set(1, balanceSheet.get(account).get(1) + post.getPostSum().getSumTotal());
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
        Map<Account, Double> incomeStatement = new HashMap<>();
        List<Verification> verifications = verRepo.findByUserAccountAndCreationDateBetween(user, startDate, endDate, pageable).getContent();

        for (Verification verification : verifications) {
            List<Post> posts = verification.getPosts();
            for (Post post : posts) {
                Account account = post.getAccount();
                if (account.getNumber() < REVENUE_ACCOUNTS) {
                    continue;
                }
                if (!incomeStatement.containsKey(account)) {
                    post.getPostSum().getSumTotal();
                    incomeStatement.put(account, post.getPostSum().getSumTotal());
                } else {
                    incomeStatement.put(account, incomeStatement.get(account) + post.getPostSum().getSumTotal());
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
    public Post findVerificationById(long id) {
        return postRepo.findOne(id);
    }
}
