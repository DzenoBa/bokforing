/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
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
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.persistence.PostRepository;
import se.chalmers.bokforing.persistence.VerificationRepository;
import se.chalmers.bokforing.persistence.VerificationSpecs;
import se.chalmers.bokforing.persistence.user.UserService;
import se.chalmers.bokforing.service.AccountManager;
import se.chalmers.bokforing.persistence.user.UserService;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.service.CustomerManager;
import se.chalmers.bokforing.service.PostService;
import se.chalmers.bokforing.service.VerificationManager;
import se.chalmers.bokforing.service.VerificationService;
import se.chalmers.bokforing.util.Constants;

/**
 *
 * @author Jakob
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class VerificationTest extends AbstractIntegrationTest {
    
    private static int INSERTED_VERIFICATION_ROWS_BEFORE;
    
    @Autowired
    private UserService userDb;
    
    @Autowired
    VerificationManager manager;
    
    @Autowired
    VerificationService service;
    
    @Autowired
    EntityManager em;
    
    @Autowired
    VerificationRepository repository;
    
    @Autowired
    CustomerManager customerManager;
    
    @Autowired
    AccountManager accountManager;
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    PostRepository postRepository;
    
    @Autowired
    UserService userService;
    
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
        INSERTED_VERIFICATION_ROWS_BEFORE = (int)verifications.getTotalElements();
    }
    
    @Transactional
    @Test
    public void testCreateVerification() {
        double sum1Amount = 100;
        double sum2Amount = 100;
        
        double sum3Amount = 200;
        double sum4Amount = 200;
       
        Calendar cal = Calendar.getInstance();

        Account account = accountManager.createAccount(2018, "Egna insättningar");
        
        PostSum sum = new PostSum();
        sum.setSumTotal(sum1Amount);
        sum.setType(PostType.Credit);
        
        PostSum sum2 = new PostSum();
        sum2.setSumTotal(sum2Amount);
        sum2.setType(PostType.Debit);
        
        PostSum sum3 = new PostSum();
        sum3.setSumTotal(sum3Amount);
        sum3.setType(PostType.Debit);
        
        PostSum sum4 = new PostSum();
        sum4.setSumTotal(sum4Amount);
        sum4.setType(PostType.Credit);
        
        Customer customer = customerManager.createCustomer(user, 123, null, null, null);
        customer.setCustomerNumber(1L);
        customer.setName("Jakob");
        customer.setPhoneNumber("031132314");
        
        Post post = new Post();
        post.setPostSum(sum);
        post.setAccount(account);
        
        Post post2 = new Post();
        post2.setPostSum(sum2);
        post2.setAccount(account);

        Post post3 = new Post();
        post3.setPostSum(sum3);
        post3.setAccount(account);
        
        Post post4 = new Post();
        post4.setPostSum(sum4);
        post4.setAccount(account);
        
        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        postList.add(post2);
        
        ArrayList<Post> postList2 = new ArrayList<>();
        postList2.add(post3);
        postList2.add(post4);
        
        Long verNbr = 7372L; // one higher than the highest inserted row
        Verification verification = manager.createVerification(user, verNbr, postList, cal.getTime(), customer);
        assertNotNull(verification);
        
        Verification verification2 = manager.createVerification(user, verNbr+1, postList2, cal.getTime(), customer);
        assertNotNull(verification2);
        
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
        
        int rowsToInsert = 30;
        
        for(int i = 0; i < rowsToInsert; ++i) {
            cal.set(2014 + i, 5, 10);
            creationDate = cal.getTime();
            
            query = em.createNativeQuery(
                "INSERT INTO Verifications (id, creationDate, verificationNumber, userAccount_id) VALUES (55555" + i + ", ?, 557, 1)")
                .setParameter(1, creationDate, TemporalType.DATE);
            query.executeUpdate();
        }
        
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate");
        Page<Verification> verifications = service.findAllVerifications(user, terms);
        
        assertEquals(Constants.DEFAULT_PAGE_SIZE, verifications.getNumberOfElements());
        assertEquals(INSERTED_VERIFICATION_ROWS_BEFORE + rowsToInsert, verifications.getTotalElements());
        
        int expectedPages = (int)Math.ceil((INSERTED_VERIFICATION_ROWS_BEFORE + rowsToInsert) / (double)Constants.DEFAULT_PAGE_SIZE);
        assertEquals(expectedPages, verifications.getTotalPages());
        
        
        List<Verification> verList = verifications.getContent();
        Date firstDate = verList.get(10).getCreationDate();
        Date secondDate = verList.get(11).getCreationDate();
        
        // Since we sort by creationDate descending (ascending argument is false),
        // dates should be "lower" the farther we go down the list
        assertTrue(firstDate.after(secondDate));
    }

    @Test
    public void testGeneralLedger() {
        createVerificationHelper();
        Account accountFromDb = accountService.findAccountByNumber(2018);

        List<Post> posts = postRepository.findPostsForUserAndAccount(user.getId(), accountFromDb.getNumber());
        assertEquals(4, posts.size());
        
        long begin = System.currentTimeMillis();
        Map<Account, List<Post>> generalLedger = postService.getGeneralLedger(user);
        System.out.println("Time to get general ledger: " + (System.currentTimeMillis() - begin));
        
        
        assertTrue(generalLedger != null);
        assertEquals(1, generalLedger.size());
        assertTrue(generalLedger.keySet().iterator().next().getNumber() == 2018);
        assertEquals(4, generalLedger.get(accountFromDb).size());
    }
    
    public void createVerificationHelper() {
        double sum1Amount = 100;
        double sum2Amount = 100;
        
        double sum3Amount = 200;
        double sum4Amount = 200;
       
        Calendar cal = Calendar.getInstance();

        Account account = accountManager.createAccount(2018, "Egna insättningar");
        
        PostSum sum = new PostSum();
        sum.setSumTotal(sum1Amount);
        sum.setType(PostType.Credit);
        
        PostSum sum2 = new PostSum();
        sum2.setSumTotal(sum2Amount);
        sum2.setType(PostType.Debit);
        
        PostSum sum3 = new PostSum();
        sum3.setSumTotal(sum3Amount);
        sum3.setType(PostType.Debit);
        
        PostSum sum4 = new PostSum();
        sum4.setSumTotal(sum4Amount);
        sum4.setType(PostType.Credit);
        
        Customer customer = customerManager.createCustomer(user, 123, null, null, null);
        customer.setCustomerNumber(1L);
        customer.setName("Jakob");
        customer.setPhoneNumber("031132314");
        
        Post post = new Post();
        post.setPostSum(sum);
        post.setAccount(account);
        
        Post post2 = new Post();
        post2.setPostSum(sum2);
        post2.setAccount(account);

        Post post3 = new Post();
        post3.setPostSum(sum3);
        post3.setAccount(account);
        
        Post post4 = new Post();
        post4.setPostSum(sum4);
        post4.setAccount(account);
        
        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        postList.add(post2);
        
        ArrayList<Post> postList2 = new ArrayList<>();
        postList2.add(post3);
        postList2.add(post4);
        
        Long verNbr = 7372L; // one higher than the highest inserted row
        Verification verification = manager.createVerification(user, verNbr, postList, cal.getTime(), customer);
        assertNotNull(verification);
        
        Verification verification2 = manager.createVerification(user, verNbr+1, postList2, cal.getTime(), customer);
        assertNotNull(verification2);
        
        Verification verificationFromDb = service.findByUserAndVerificationNumber(user, verNbr);
        assertNotNull(verificationFromDb);
    }
    
    @Test
    @Transactional
    public void testReplacePost() {
        Long verificationNumber = 7372L;
        Account account = accountManager.createAccount(2018, "Egna insättningar");
        
        createVerificationHelper();
        
        // Fail case, this won't work, balance will not be zero
        Verification ver = service.findByUserAndVerificationNumber(user, verificationNumber);
        
        PostSum postSum = new PostSum();
        postSum.setSumTotal(50);
        postSum.setType(PostType.Debit);
        
        Post newPost = new Post();
        newPost.setPostSum(postSum);
        newPost.setAccount(account);
        newPost.setCorrection(true);
        
        List<Post> verificationPosts = ver.getPosts();
        Post postToReplace = verificationPosts.get(0);
        boolean success = manager.replacePost(ver, postToReplace, newPost);
        assertFalse(success);
        
        // Success case, replacing with same data
        ver = service.findByUserAndVerificationNumber(user, verificationNumber);
        
        PostSum postSum2 = new PostSum();
        postSum2.setSumTotal(100);
        postSum2.setType(PostType.Credit);
        
        Post newPost2 = new Post();
        newPost2.setPostSum(postSum2);
        newPost2.setAccount(account);
        newPost2.setCorrection(true);
        
        Post postToReplace2 = verificationPosts.get(0);
        boolean success2 = manager.replacePost(ver, postToReplace2, newPost2);
        assertTrue(success2);
        
        // Set it back to the way it was
        assertTrue(manager.replacePost(ver, newPost2, postToReplace2));
        
        // Replace both posts
        ver = service.findByUserAndVerificationNumber(user, verificationNumber);
        
        PostSum postSum3 = new PostSum();
        postSum3.setSumTotal(40);
        postSum3.setType(PostType.Credit);
        
        Post newPost3 = new Post();
        newPost3.setPostSum(postSum3);
        newPost3.setAccount(account);
        newPost3.setCorrection(true);
        
        PostSum postSum4 = new PostSum();
        postSum4.setSumTotal(40);
        postSum4.setType(PostType.Debit);
        
        Post newPost4 = new Post();
        newPost4.setPostSum(postSum4);
        newPost4.setAccount(account);
        newPost4.setCorrection(true);
        
        List<Post> postsToReplaceList = ver.getPosts();
        List<Post> newPostsList = Arrays.asList(newPost3, newPost4);
        boolean success3 = manager.replacePost(ver, postsToReplaceList, newPostsList);
        assertTrue(success3);
        
        // Need to make sure they are all marked as corrected
        List<Post> postsFromDb = service.findByUserAndVerificationNumber(user, verificationNumber).getPosts();
        for(Post post : postsFromDb) {
            assertTrue(post.isCorrection());
        }
        
        // Make sure posts were actually inserted
        assertEquals(newPostsList.get(0), postsFromDb.get(0));
    }
}
