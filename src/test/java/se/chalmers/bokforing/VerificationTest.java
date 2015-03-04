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
        Calendar cal = Calendar.getInstance();
        Account account = new Account();
        account.setName("Egna insättningar");
        account.setNumber("2018");
        
        PostSum sum = new PostSum();
        sum.setSumTotal(100);
        sum.setType(PostType.Credit);
        
        Customer customer = new Customer();
        customer.setName("Jakob");
        customer.setPhoneNumber("031132314");
        
        Post post = new Post();
        post.setSum(sum);
        post.setAccount(account);
        
        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        
        String verNbr = "123";
        manager.createVerification(verNbr, postList, cal.getTime(), customer);
        Verification verification = service.findVerificationById(verNbr);
    }
    
}
