/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.UserAccount;
import org.springframework.data.domain.Pageable;
import se.chalmers.bokforing.model.AccountType;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;

/**
 *
 * @author Jakob
 */
public interface PostService {

    Map<Account, List<Post>> getGeneralLedger(UserAccount user);

    void save(Post post);

    Map<Account, List<Double>> getBalanceSheet(UserAccount user, Date startDate, Date endDate, Pageable pageable);

    Map<Account, Double> getIncomeStatement(UserAccount user, Date startDate, Date endDate, Pageable pageable);

    Page<Post> findPostsForUserAndAccount(UserAccount user, Account account, boolean isActive, PagingAndSortingTerms terms);

    Post findPostById(long id);
    
    double getBalanceForAccountTypeBetweenDates(UserAccount user, AccountType accountType, Date start, Date end);

    Map<Date, Double> getBalanceForAccountAtDate(UserAccount user, AccountType accountType, Date startDate, Date endDate);
}
