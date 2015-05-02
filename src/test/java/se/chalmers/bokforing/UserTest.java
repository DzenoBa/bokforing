/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.service.AccountManager;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.service.UserManager;

/**
 *
 * @author Jakob
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class UserTest extends AbstractIntegrationTest {
    
    @Autowired
    private UserManager userManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AccountManager accountManager;
    
    @Autowired
    private AccountService accountService;
    
    private UserAccount user;
    
    private UserHandler handler;
    
    @Before
    public void setup() {
        handler = userService.getUser("apa@test.com");
        user = handler.getUA();
        assertNotNull(handler);
        assertNotNull(user);
    }
    
    @Test
    @Transactional
    public void testAddFavoriteAccount() {
        Account account = accountManager.createAccount(1510, "Favoritkonto");
        assertNotNull(account);
        
        userManager.addFavoriteAccount(handler, account);
        
        UserAccount userFromDb = userService.getUser(user.getEmail()).getUA();
        assertNotNull(userFromDb);
        
        Set<Account> expectedFavoriteAccounts = new HashSet<>();
        expectedFavoriteAccounts.add(account);
        assertEquals(expectedFavoriteAccounts, userFromDb.getFavoriteAccounts());
    }

    @Test
    @Transactional
    public void testRemoveFavoriteAccount() {
        
        Account account = accountManager.createAccount(1510, "Favoritkonto");
        assertNotNull(account);
        
        // There is a test for this to be working, so we don't need to check
        // that anything was actually added
        userManager.addFavoriteAccount(handler, account);
        
        userManager.removeFavoriteAccount(handler, account);
        
        UserAccount userFromDb = userService.getUser(user.getEmail()).getUA();
        assertNotNull(userFromDb);
        
        Set<Account> expectedFavoriteAccounts = new HashSet<>();
        
        assertEquals(expectedFavoriteAccounts, userFromDb.getFavoriteAccounts());
    }
}
