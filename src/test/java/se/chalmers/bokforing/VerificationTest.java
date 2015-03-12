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
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.model.PostType;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.persistence.VerificationRepository;
import se.chalmers.bokforing.persistence.VerificationSpecs;
import se.chalmers.bokforing.service.VerificationManager;
import se.chalmers.bokforing.service.VerificationService;
import se.chalmers.bokforing.util.Constants;

/**
 *
 * @author Jakob
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class VerificationTest extends AbstractIntegrationTest {
    
    private static final int INSERTED_VERIFICATION_ROWS_BEFORE = 5;
    
    @Autowired
    VerificationManager manager;
    
    @Autowired
    VerificationService service;
    
    @Autowired
    EntityManager em;
    
    @Autowired
    VerificationRepository repository;
    
    private static UserAccount user;
    
    @Before
    public void setup() {
        user = new UserAccount();
        user.setId(1L);
    }
    
    @Test
    public void testCreateVerification() {
        double sum1Amount = 100;
        double sum2Amount = 100;
        
        Calendar cal = Calendar.getInstance();
        Account account = new Account();
        account.setName("Egna insättningar");
        account.setNumber(2018);
        
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
        
        int verNbr = 7372; // one higher than the highest inserted row
        Verification verification = manager.createVerification(user, verNbr, postList, cal.getTime(), customer);
        assertNotNull(verification);
        
        Verification verificationFromDb = service.findByUserAndVerificationNumber(user, verNbr);
        assertNotNull(verificationFromDb);
    }
    
    @Test
    public void testFindHighestVerificationId() {
        long highestId = service.findHighestVerificationNumber(user);   
        
        // From the inserted rows
        assertEquals(7371, highestId);
    }
    
    @Transactional
    @Test
    public void testFindVerificationBetweenDates() {
        Calendar cal = Calendar.getInstance();
        
        cal.set(2014, 5, 10);
        Date creationDateInsideRange = cal.getTime();
        
        cal.set(2014, 5, 21);
        Date creationDateJustAfterRange = cal.getTime();
        
        cal.set(2014, 5, 4);
        Date creationDateJustBeforeRange = cal.getTime();
        
        cal.set(2014, 5, 5);
        Date startDate = cal.getTime();
        
        cal.set(2014, 5, 20);
        Date endDate = cal.getTime();
        
        Query query1 = em.createNativeQuery(
                "INSERT INTO Verifications (id, creationDate, userAccount_id, verificationNumber) VALUES (999, ?, 1, 555)")
                .setParameter(1, creationDateInsideRange, TemporalType.DATE);
        query1.executeUpdate();
        
        Query query2 = em.createNativeQuery(
                "INSERT INTO Verifications (id, creationDate, userAccount_id, verificationNumber) VALUES (1000, ?, 1, 556)")
                .setParameter(1, creationDateJustAfterRange, TemporalType.DATE);
        query2.executeUpdate();
        
        Query query3 = em.createNativeQuery(
                "INSERT INTO Verifications (id, creationDate, userAccount_id, verificationNumber) VALUES (1001, ?, 1, 557)")
                .setParameter(1, creationDateJustBeforeRange, TemporalType.DATE);
        query3.executeUpdate();
        
        assertEquals(INSERTED_VERIFICATION_ROWS_BEFORE + 3, repository.findAll().size());
        
        Page<Verification> vers = repository.findByUserAccountAndCreationDateBetween(user, startDate, endDate, null);
        
        assertEquals(1, vers.getTotalElements());
    }
    
    @Transactional
    @Test
    public void testGetVerificationPageCorrectParameters() {
        Calendar cal = Calendar.getInstance();
        Date creationDate = null;
        Query query = null;
        
        for(int i = 0; i < 100; ++i) {
            cal.set(2014 + i, 5, 10);
            creationDate = cal.getTime();
            
            query = em.createNativeQuery(
                "INSERT INTO Verifications (id, creationDate, verificationNumber) VALUES (55555" + i + ", ?, 557)")
                .setParameter(1, creationDate, TemporalType.DATE);
            query.executeUpdate();
        }
        
        Page<Verification> verifications = service.findAllVerifications(user, 0, "creationDate", false);
        
        assertEquals(Constants.DEFAULT_PAGE_SIZE, verifications.getNumberOfElements());
        assertEquals(INSERTED_VERIFICATION_ROWS_BEFORE + 100, verifications.getTotalElements());
        
        int expectedPages = (int)Math.ceil((INSERTED_VERIFICATION_ROWS_BEFORE + 100) / (double)Constants.DEFAULT_PAGE_SIZE);
        assertEquals(expectedPages, verifications.getTotalPages());
        
        
        List<Verification> verList = verifications.getContent();
        Date firstDate = verList.get(10).getCreationDate();
        Date secondDate = verList.get(11).getCreationDate();
        
        // Since we sort by creationDate descending (ascending argument is false),
        // dates should be "lower" the farther we go down the list
        assertTrue(firstDate.after(secondDate));
    }
    
    @Transactional
    @Test
    public void testGetVerificationForUser() {
        // Have a user account
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1L);
        
        // This is pretty much:
        //  select * 
        //  from Verifications v 
        //  where v.userAccount = userAccount
        List<Verification> vers = repository.findAll(Specifications.where(VerificationSpecs.hasUserAccount(userAccount)));
        assertTrue(vers.get(0).getUserAccount().getId() == 1);
    }
}
