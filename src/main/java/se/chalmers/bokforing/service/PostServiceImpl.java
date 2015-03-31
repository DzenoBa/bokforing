/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.persistence.PostRepository;
import static se.chalmers.bokforing.util.Constants.REVENUE_ACCOUNTS;

/**
 *
 * @author Jakob
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository repo;

    @Autowired
    private AccountService accountService;

    @Override
    public Map<Account, List<Post>> getGeneralLedger(UserAccount user) {
        Map<Account, List<Post>> generalLedger = new HashMap<>();

        List<Account> accounts = accountService.findAllAccounts();
        for (Account account : accounts) {
            List<Post> posts = repo.findPostsForUserAndAccount(user.getId(), account.getNumber());

            generalLedger.put(account, posts);
        }

        return generalLedger;
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public Map<Account, Integer> getBalanceSheet(UserAccount user) {
        Map<Account, Integer> balanceSheet = new HashMap<>();
        List<Account> accounts = accountService.findAllAccounts();
        for (Account account : accounts) {
            if (account.getNumber() >= REVENUE_ACCOUNTS) {
                return balanceSheet;
            }
            List<Post> posts = repo.findPostsForUserAndAccount(user.getId(), account.getNumber());
            int balance = 0;
            for (Post post : posts) {
                balanceSheet.put(account, balance);
            }
        }
        return balanceSheet;
    }

}
