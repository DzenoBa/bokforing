/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import se.chalmers.bokforing.VerificationTest;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.PostServiceImpl;

/**
 *
 * @author Isabelle
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class PostServiceImplTest {
    @Autowired
    VerificationTest test;

    /**
     * Test of getBalanceSheet method, of class PostServiceImpl.
     */
    @Test
    public void testGetBalanceSheet() {
        System.out.println("getBalanceSheet");
        test.createVerificationHelper();
        UserAccount user = null;
        Date startDate = null;
        Date endDate = null;
        Pageable pageable = null;
        PostServiceImpl instance = new PostServiceImpl();
        Map<Account, List<Double>> expResult = null;
        Map<Account, List<Double>> result = instance.getBalanceSheet(user, startDate, endDate, pageable);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIncomeStatement method, of class PostServiceImpl.
     */
    @Test
    public void testGetIncomeStatement() {
        System.out.println("getIncomeStatement");
        UserAccount user = null;
        Date startDate = null;
        Date endDate = null;
        Pageable pageable = null;
        PostServiceImpl instance = new PostServiceImpl();
        Map<Account, Double> expResult = null;
        Map<Account, Double> result = instance.getIncomeStatement(user, startDate, endDate, pageable);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findPostsForUserAndAccount method, of class PostServiceImpl.
     */
    @Test
    public void testFindPostsForUserAndAccount() {
        System.out.println("findPostsForUserAndAccount");
        UserAccount user = null;
        Account account = null;
        boolean isActive = false;
        PagingAndSortingTerms terms = null;
        PostServiceImpl instance = new PostServiceImpl();
        Page<Post> expResult = null;
        Page<Post> result = instance.findPostsForUserAndAccount(user, account, isActive, terms);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findVerificationById method, of class PostServiceImpl.
     */
    @Test
    public void testFindVerificationById() {
        System.out.println("findVerificationById");
        long id = 0L;
        PostServiceImpl instance = new PostServiceImpl();
        Post expResult = null;
        Post result = instance.findVerificationById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
