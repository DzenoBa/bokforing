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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.AccountType;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.model.PostType;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.persistence.PostRepository;
import se.chalmers.bokforing.persistence.VerificationRepository;
import se.chalmers.bokforing.service.AccountManager;
import se.chalmers.bokforing.persistence.user.UserService;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.service.CustomerManager;
import se.chalmers.bokforing.service.PostManager;
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
    
    //@Autowired
    //private UserService userDb;
    
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
    
    @Autowired
    PostManager postManager;
    
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
        Verification verification = manager.createVerification(user, verNbr, postList, cal.getTime(), customer, "");
        assertNotNull(verification);
        
        Verification verification2 = manager.createVerification(user, verNbr+1, postList2, cal.getTime(), customer, "");
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
    @Transactional
    public void testGeneralLedger() {
        createVerificationHelper();
        Account accountFromDb = accountService.findAccountByNumber(2018);

        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate");
        List<Post> posts = postService.findPostsForUserAndAccount(user, accountFromDb, true, terms).getContent();
        assertEquals(4, posts.size());
        
        long begin = System.currentTimeMillis();
        Map<Account, List<Post>> generalLedger = postService.getGeneralLedger(user);
        System.out.println("Time to get general ledger: " + (System.currentTimeMillis() - begin));
        
        
        assertTrue(generalLedger != null);
        assertEquals(1, generalLedger.size());
        assertTrue(generalLedger.keySet().iterator().next().getNumber() == 2018);
        assertEquals(4, generalLedger.get(accountFromDb).size());
    }
    
    @Transactional
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
        
        Customer customer = customerManager.createCustomer(user, 123, "Jakob", "0531531", null);
        
        Post post = postManager.createPost(sum, account);
        Post post2 = postManager.createPost(sum2, account);
        Post post3 = postManager.createPost(sum3, account);
        Post post4 = postManager.createPost(sum4, account);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        postList.add(post2);
        
        ArrayList<Post> postList2 = new ArrayList<>();
        postList2.add(post3);
        postList2.add(post4);
        
        Long verNbr = 7372L; // one higher than the highest inserted row
        Verification verification = manager.createVerification(user, verNbr, postList, cal.getTime(), customer, "");
        assertNotNull(verification);
        
        Verification verification2 = manager.createVerification(user, verNbr+1, postList2, cal.getTime(), customer, "");
        assertNotNull(verification2);
        
        Verification verificationFromDb = service.findByUserAndVerificationNumber(user, verNbr);
        assertNotNull(verificationFromDb);
    }
    
    @Test
    @Transactional
    public void testReplacePostsWithInvalidPosts() {
        Long verificationNumber = 7372L;
        Account account = accountManager.createAccount(2018, "Egna insättningar");
        
        createVerificationHelper();
        
        // Fail case, this won't work, since balance will not be zero
        Verification ver = service.findByUserAndVerificationNumber(user, verificationNumber);
        
        PostSum postSum = new PostSum();
        postSum.setSumTotal(50);
        postSum.setType(PostType.Debit);
        
        Post newPost = postManager.createPost(postSum, account);
        
        List<Post> verificationPosts = ver.getPosts();
        Post postToReplace = verificationPosts.get(0);
        boolean success = manager.replacePost(ver, postToReplace, newPost);
        assertFalse(success);
        
        // Since replace failed, post is not set as inactive
        assertTrue(postToReplace.isActive());
        assertFalse(postToReplace.isCorrection());
        
        Verification verFromDb = service.findByUserAndVerificationNumber(user, verificationNumber);
        assertFalse(verFromDb.getPosts().contains(newPost));

    }
    
    @Test
    @Transactional
    public void testReplacePostWithValidPosts() {
        Long verificationNumber = 7372L;
        Account account = accountManager.createAccount(2018, "Egna insättningar");
        
        createVerificationHelper();
        Verification ver = service.findByUserAndVerificationNumber(user, verificationNumber);
        List<Post> verificationPosts = ver.getPosts();
        
        // Success case, replacing with same data
        
        PostSum postSum = new PostSum();
        postSum.setSumTotal(100);
        postSum.setType(PostType.Credit);
        
        Post newPost = postManager.createPost(postSum, account);
        
        Post postToReplace = verificationPosts.get(0);
        boolean success = manager.replacePost(ver, postToReplace, newPost);
        assertTrue(success);
        
        assertFalse(postToReplace.isActive());
        assertFalse(postToReplace.isCorrection());
        
        Verification verFromDb = service.findByUserAndVerificationNumber(user, verificationNumber);
        assertTrue(verFromDb.getPosts().contains(newPost));
    }
    
    @Test
    @Transactional
    public void testReplacePostWithList() {
        Long verificationNumber = 7372L;
        Account account = accountManager.createAccount(2018, "Egna insättningar");
        
        createVerificationHelper();
        
        // Success case, replace all posts
        Verification ver = service.findByUserAndVerificationNumber(user, verificationNumber);
        
        // Saved for later
        List<Post> originalPosts = ver.getPosts();
        
        PostSum postSum = new PostSum();
        postSum.setSumTotal(40);
        postSum.setType(PostType.Credit);
        Post newPost = postManager.createPost(postSum, account);
        
        PostSum postSum2 = new PostSum();
        postSum2.setSumTotal(40);
        postSum2.setType(PostType.Debit);
        Post newPost2 = postManager.createPost(postSum2, account);
        
        List<Post> postsToReplaceList = ver.getPosts();
        
        List<Post> newPostsList = Arrays.asList(newPost, newPost2);
        boolean success3 = manager.replacePost(ver, postsToReplaceList, newPostsList);
        assertTrue(success3);
        
        List<Post> postsFromDb = service.findByUserAndVerificationNumber(user, verificationNumber).getPosts();
        
        // Posts that have been replacedshould not be active
        for(Post replacedPost : postsToReplaceList) {
            assertFalse(replacedPost.isActive());
            assertFalse(replacedPost.isCorrection());
        }
        
        // Posts that have been added should be active and marked as correction
        for(Post post : newPostsList) {
            assertTrue(post.isActive());
            assertTrue(post.isCorrection());
        }
        
        List<Post> postsThatShouldBeInDb = Arrays.asList(originalPosts.get(0), originalPosts.get(1), newPost, newPost2);
        
        // Make sure posts were actually inserted
        assertEquals(postsThatShouldBeInDb, postsFromDb);
    }
    
    @Test
    @Transactional
    public void testGetPost() {
        createVerificationHelper();
        
        Verification ver = service.findByUserAndVerificationNumber(user, 7372);
        
        Post postFromVer = ver.getPosts().get(0);
        Long postId = postFromVer.getId();
        
        Post postFromDb = postService.findPostById(postId);
        
        assertEquals(postFromVer, postFromDb);
        assertEquals(postFromVer.getId(), postFromDb.getId());
    }
    
    @Test
    @Transactional
    public void testGetPostsForAccountTypeBetweenDates() {
        double sum1Amount = 100;
        double sum2Amount = 100;
        
        double sum3Amount = 200;
        double sum4Amount = 200;
       
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 10, 10);

        Account account = accountManager.createAccount(2018, "Egna insättningar");
        Account account2 = accountManager.createAccount(1055, "Saker");
        
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
        
        Customer customer = customerManager.createCustomer(user, 123, "Jakob", "03143535", null);
        
        Post post = postManager.createPost(sum, account);
        Post post2 = postManager.createPost(sum2, account2);
        Post post3 = postManager.createPost(sum3, account);
        Post post4 = postManager.createPost(sum4, account2);
        
        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        postList.add(post2);
        
        ArrayList<Post> postList2 = new ArrayList<>();
        postList2.add(post3);
        postList2.add(post4);
        
        Long verNbr = 7372L; // one higher than the highest inserted row
        Verification verification = manager.createVerification(user, verNbr, postList, cal.getTime(), customer, "");
        assertNotNull(verification);
        
        Verification verification2 = manager.createVerification(user, verNbr+1, postList2, cal.getTime(), customer, "");
        assertNotNull(verification2);
        
        Verification verificationFromDb = service.findByUserAndVerificationNumber(user, verNbr);
        assertNotNull(verificationFromDb);
        
        cal.set(2014, 10, 9);
        Date startDate = cal.getTime();
        
        cal.set(2014, 10, 11);
        Date endDate = cal.getTime();
        
        // Assets -> account2 (because it starts with a 1) -> posts 2 and 4
        double balance = postService.getBalanceForAccountTypeBetweenDates(user, AccountType.ASSETS, startDate, endDate);
        
        double expectedBalance = post2.getBalance() + post4.getBalance();
        assertTrue(expectedBalance == balance);
    }
}
