/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.model.PostType;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.persistence.user.UserService;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.service.PostService;
import se.chalmers.bokforing.service.PostServiceImpl;
import se.chalmers.bokforing.service.VerificationService;

/**
 *
 * @author Isabelle
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class PostServiceImplTest {

    private static int INSERTED_VERIFICATION_ROWS_BEFORE;

    @Autowired
    VerificationTest test;

    @Autowired
    UserService userService;

    @Autowired
    VerificationService service;

    @Autowired
    AccountService accountService;

    @Autowired
    PostService postService;

    private UserAccount user;

    @Before
    public void setup() {
        user = userService.getUser("apa@test.com").getUA();

        //user.setId(1L);
        user.getId();
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate");
        Page<Verification> verifications = service.findAllVerifications(user, terms);
        INSERTED_VERIFICATION_ROWS_BEFORE = (int) verifications.getTotalElements();
    }

    /**
     * Test of getBalanceSheet method, of class PostServiceImpl.
     */
    @Test
    @Transactional
    public void testGetBalanceSheet() {
        Calendar cal = Calendar.getInstance();

        Account accountFromDb = accountService.findAccountByNumber(2018);
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate");
        List<Post> posts = postService.findPostsForUserAndAccount(user, accountFromDb, true, terms).getContent();
        assertEquals(4, posts.size());

        //Krångligt
        cal.set(0000, 00, 00);
        Date startDate = cal.getTime();
        cal.set(3000, 01, 01);
        Date endDate = cal.getTime();
        PostServiceImpl instance = new PostServiceImpl();

        long begin = System.currentTimeMillis();
        Map<Account, List<Double>> result = instance.getBalanceSheet(user, startDate, endDate, null);
        System.out.println("Time to get Balance Sheet: " + (System.currentTimeMillis() - begin));
        List<Double> expResult = new ArrayList<>();

        Double totalSum = posts.iterator().next().getPostSum().getSumTotal();
        while (posts.iterator().hasNext()) {

            totalSum += posts.iterator().next().getPostSum().getSumTotal();
        }
        System.out.println(totalSum);

        expResult.add(0, totalSum);
        expResult.add(1, 0.0);
        assertEquals(result.get(accountFromDb), expResult);
    }

    /**
     * Test of getIncomeStatement method, of class PostServiceImpl.
     */
    @Test
    public void testGetIncomeStatement() {
        System.out.println("getIncomeStatement");
        Date startDate = null;
        Date endDate = null;
        Pageable pageable = null;
        PostServiceImpl instance = new PostServiceImpl();
        Map<Account, Double> expResult = null;
        Map<Account, Double> result = instance.getIncomeStatement(user, startDate, endDate, pageable);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
