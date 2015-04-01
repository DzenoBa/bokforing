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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.user.UserAccount;
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
            List<Post> posts = postRepo.findPostsForUserAndAccount(user.getId(), account.getNumber());

            generalLedger.put(account, posts);
        }

        return generalLedger;
    }

    /**
     *
     * @param user
     * @return balanceSheet, a mapping from account to the sum of all posts
     * using that account and the opening balance. Other things needed to create
     * the full balanceSheet is title, company name, period and so on.
     */
    @Override
    public Map<Account, List<Double>> getBalanceSheet(UserAccount user, Date startDate, Date endDate, Pageable pageable) {
        Map<Account, List<Double>> balanceSheet = new HashMap<>();
        List<Verification> verifications = verRepo.findByUserAccountAndCreationDateBetween(user, startDate, endDate, pageable).getContent();
        List<Account> accounts = accountService.findAllAccounts();
        for (Verification verification : verifications) {
            List<Post> posts = verification.getPosts();
            for (Post post : posts) {
                Account account = post.getAccount();
                if (account.getNumber() >= REVENUE_ACCOUNTS) {
                    continue;
                }
                if (!balanceSheet.containsKey(account)) {
                    List<Double> balanceList = new ArrayList<>();
                    balanceList.add(post.getPostSum().getSumTotal());
                    balanceSheet.put(account, balanceList);
                } else {
                    //TODO: FELFELFEL
                    List<Double> balanceList = balanceSheet.get(account);
                    double  balance = balanceList.get(0);
                    balance += post.getPostSum().getSumTotal();
                    balanceList.clear();
                    balanceList.add(balance);
                    balanceSheet.put(account, balanceList);
                }
            }

            for (Account account : accounts) {

                List<Double> balances = new ArrayList<>();
                double balance = 0;
                for (Post post : posts) {
                    balance += post.getPostSum().getSumTotal();

                }
                balances.add(balance);
                balanceSheet.put(account, balances);
            }
        }
        return balanceSheet;
    }

}
