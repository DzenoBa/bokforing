/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import java.util.ArrayList;
import java.util.Calendar;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.model.PostType;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.service.VerificationManager;
import se.chalmers.bokforing.service.VerificationService;

/**
 *
 * @author Jakob
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class VerificationTest extends AbstractIntegrationTest {
    
    @Autowired
    VerificationManager manager;
    
    @Autowired
    VerificationService service;
    
    @Test
    public void testCreateVerification() {
        double sum1Amount = 100;
        double sum2Amount = 100;
        
        Calendar cal = Calendar.getInstance();
        Account account = new Account();
        account.setName("Egna insättningar");
        account.setNumber("2018");
        
        PostSum sum = new PostSum();
        sum.setSumTotal(sum1Amount);
        sum.setType(PostType.Credit);
        
        PostSum sum2 = new PostSum();
        sum2.setSumTotal(sum2Amount);
        sum2.setType(PostType.Debit);
        
        Customer customer = new Customer();
        customer.setName("Jakob");
        customer.setPhoneNumber("031132314");
        
        Post post = new Post();
        post.setSum(sum);
        post.setAccount(account);
        
        Post post2 = new Post();
        post2.setSum(sum2);
        post2.setAccount(account);
        
        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        postList.add(post2);
        
        int verNbr = 126; // one higher than the highest inserted row
        Verification verification = manager.createVerification(verNbr, postList, cal.getTime(), customer);
        assertNotNull(verification);
        
        Verification verificationFromDb = service.findVerificationById(verNbr);
        assertNotNull(verificationFromDb);
    }
    
    @Test
    public void testFindHighestVerificationId() {
        long highestId = service.findHighestId();
        
        // From the inserted rows
        assertEquals(125, highestId);
    }
    
}
