
package se.chalmers.bokforing.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.jsonobject.AccountJSON;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.PostJSON;
import se.chalmers.bokforing.jsonobject.VerificationJSON;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.model.PostType;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.persistence.PostRepository;
import se.chalmers.bokforing.service.CustomerManager;
import se.chalmers.bokforing.service.CustomerService;
import se.chalmers.bokforing.persistence.user.UserService;
import se.chalmers.bokforing.service.AccountManager;
import se.chalmers.bokforing.service.VerificationManager;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.service.PostService;
import se.chalmers.bokforing.service.VerificationService;
import se.chalmers.bokforing.session.AuthSession;

/**
 *
 * @author Dženan
 */
@Controller
public class BookkeepingController {
    
    @Autowired
    private VerificationManager verificationManager;
    
    @Autowired
    private VerificationService verificationService;
    
    @Autowired
    AccountService accountService;
    
    @Autowired 
    private AuthSession authSession;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomerManager customerManager;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private AccountManager accountManager;
    
    @Autowired
    private PostService postService; //TODO
    
    /*
     * CREATE
     */
    @RequestMapping(value = "/bookkeeping/createman", method = RequestMethod.POST)
    public @ResponseBody FormJSON createman(@RequestBody final VerificationJSON verification) {
        
        System.out.println("* PING bookkeeping/createman");
        FormJSON form = new FormJSON();
        
        // CHECK SESSION
        if(!(authSession.sessionCheck())) {
            form.addError("general", "Ett fel inträffades, du har inte rätt tillstånd för att utföra denna åtgärd!");
            return form;
        }
        String email = authSession.getEmail();
        
        // CHECK DATE
        if(verification.getTransactionDate() == null) {
            form.addError("verificationdate", "Ange ett datum");
            return form;
        }
        
        // CHECK POSTS
        if(verification.getPosts() == null || verification.getPosts().isEmpty()) {
            form.addError("general", "Ett fel inträffades vänligen försök igenom om en liten stund");
            return form;
        }
            
        // CREATE POSTS
        List<Post> new_posts = new ArrayList();
        for(PostJSON post : verification.getPosts()) {
            if(post == null) {
                form.addError("general", "Ett fel inträffades, vänligen försök igen om en liten stund.");
                return form;
            }
            
            int index = new_posts.size() + 1;
            // CHECK ACCOUNT
            if(!(post.getAccountid() > 0)) {
                form.addError("general", "Vänligen välj ett konto för rad " + index + "!");
                return form;
            } 
            Account temp_account = accountService.findAccountByNumber(post.getAccountid());
            // THIS SHOULD'NT HAPPEN 
            if(temp_account == null) {
                form.addError("general", "Något gick fel, vänligen försök igen om en liten stund");
            }
            // CHECK DEBIT AND CREDIT
            if(post.getDebit() < 0 || post.getCredit() < 0) {
                form.addError("general", "Både debet och kredit måste innehålla en siffra i rad " + index + "!");
                return form;
            }
            
            // CREATE A POSTSUM
            PostSum temp_postSum = new PostSum();
            
            // DEBIT
            if (post.getDebit() > 0 && post.getCredit() == 0) {
                temp_postSum.setType(PostType.Debit);
                temp_postSum.setSumTotal(post.getDebit());
            }
            // CREDIT
            else if (post.getCredit() > 0 && post.getDebit() == 0) {
                temp_postSum.setType(PostType.Credit);
                temp_postSum.setSumTotal(post.getCredit());
            }
            // SOMETHING WRONG
            else {
                form.addError("general", "Debet och Kredit får ej vara lika eller en av dem måste vara 0; Rad: " + index);
                return form;
            }
            
            // EVERYTHING SEEMS TO BE IN ORDER; CREATE POST
            Post temp_post = new Post();
            temp_post.setAccount(temp_account);
            temp_post.setPostSum(temp_postSum);
            
            new_posts.add(temp_post);
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER; CREATE VERIFICATION
        UserHandler uh = userService.getUser(email);
        //userService.storeUser(user);
        
        // TODO
        long customerNumber = 123;
        Customer customer = customerManager.createCustomer(uh.getUA(), customerNumber, "Dzeno", "00387", null);
        Customer customerFromDb = customerService.findByCustomerNumber(uh.getUA(), customerNumber);
        
        // CREATE VERIFICATION
        Verification ver = verificationManager.createVerification(uh.getUA(), new_posts, verification.getTransactionDate(), customerFromDb, "");
        uh.getVerifications().add(ver);
        userService.storeUser(uh);
        
        // System.out.println(userService.getUser(authSession.getEmail()).getVerifications().get(0));
        
        return form;
    }
    
    @RequestMapping(value = "/bookkeeping/searchaccount", method = RequestMethod.POST)
    public @ResponseBody List<Account> searchaccount(@RequestBody final AccountJSON account) {
        List<Account> accLs;
        int start = 0;
        
        if(account.getStartrange() > 0) {
            start = account.getStartrange();
        }
        
        if(account.getNumber() > 0) {
            PagingAndSortingTerms terms = new PagingAndSortingTerms(start, true, "number");
            accLs = accountService.findByNumberLike(account.getNumber(), terms).getContent();
        } else if (!(account.getName().isEmpty())) {
            PagingAndSortingTerms terms = new PagingAndSortingTerms(start, true, "name");
            accLs = accountService.findByNameLike(account.getName(), terms).getContent();
        } else {
            // Return a empty list
            accLs = new ArrayList();
        }
        return accLs;
    }
    
    @RequestMapping(value = "/bookkeeping/countsearchaccount", method = RequestMethod.POST)
    public @ResponseBody long countsearchaccount(@RequestBody final AccountJSON account) {
        long size = 0;
        
        if(account.getNumber() > 0) {
            PagingAndSortingTerms terms = new PagingAndSortingTerms(0, true, "number");
            size = accountService.findByNumberLike(account.getNumber(), terms).getTotalElements();
        } else if (!(account.getName().isEmpty())) {
            PagingAndSortingTerms terms = new PagingAndSortingTerms(0, true, "name");
            size = accountService.findByNameLike(account.getName(), terms).getTotalElements();
        } 
        return size;
    }
    
    @RequestMapping(value = "/bookkeeping/getverifications", method = RequestMethod.GET)
    public @ResponseBody List<VerificationJSON> getVerifications() {
        List<VerificationJSON> verJSONLs = new ArrayList();
        
        if(!authSession.sessionCheck()) {
            return verJSONLs;
        } 
        UserHandler ua = userService.getUser(authSession.getEmail());
        
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate"); // TODO
        Page<Verification> verPage = verificationService.findAllVerifications(ua.getUA(), terms);
        
        List<Verification> verLs = verPage.getContent();
        
        verJSONLs = convertToVerificationJSONLs(verLs);
        return verJSONLs;
    }
    
    @RequestMapping(value = "/bookkeeping/getverificationsbyaccount", method = RequestMethod.POST)
    public @ResponseBody List<VerificationJSON> getVerificationByAccount(@RequestBody final AccountJSON account) {
        
        List<VerificationJSON> verJSONLs = new ArrayList();
        
        if(!authSession.sessionCheck()) {
            return verJSONLs;
        } 
        UserHandler userHandler = userService.getUser(authSession.getEmail());
        
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, null);
        Account entityAccount = new Account();
        entityAccount.setNumber(account.getNumber());
        List<Post> postLs = postService.findPostsForUserAndAccount(userHandler.getUA(), entityAccount, terms).getContent();
        
        int i = 0; // TODO
        List<Verification> verLs = new ArrayList();
        for(Post post : postLs) {
            verLs.add(post.getVerification());
            i++;
            if(i>=20)
                break; // TODO
        }
        verJSONLs = convertToVerificationJSONLs(verLs);
        
        return verJSONLs;
    }
    
    /**
     * CONVERT TO VERIFICATIONJSON LIST
     * 
     * Takes a Verification-List and
     * converts it to a VerificationJSON-List
     * 
     * @param verLs
     * @return 
     */
    public List<VerificationJSON> convertToVerificationJSONLs(List<Verification> verLs) {
        List<VerificationJSON> verJSONLs = new ArrayList();
        
        // TRANSLATE VERIFICATION TO VERIFICATION JSON
        for (Verification ver : verLs) {
            VerificationJSON verJSON = new VerificationJSON();
            
            verJSON.setId(ver.getId());
            verJSON.setTransactionDate(ver.getTransactionDate());
            verJSON.setCreationDate(ver.getCreationDate());
            
            // CREATE POST-JSON'S
            List<PostJSON> debitPostJSONLs = new ArrayList();
            List<PostJSON> creditPostJSONLs = new ArrayList();
            for (Post post : ver.getPosts()) {
                PostJSON postJSON = new PostJSON();
                postJSON.setAccountid(post.getAccount().getNumber());
                postJSON.setAccountname(post.getAccount().getName());
                postJSON.setSum(post.getPostSum().getSumTotal());
                
                // ADD POST TO RIGHT LS
                if(post.getPostSum().getType().equals(PostType.Debit)) {
                    debitPostJSONLs.add(postJSON);
                } else if(post.getPostSum().getType().equals(PostType.Credit)) {
                    creditPostJSONLs.add(postJSON);
                }
            }
            verJSON.setDebitposts(debitPostJSONLs);
            verJSON.setCreditposts(creditPostJSONLs);
            
            // CALC THE TOTAL SUM
            // TOTAL DEBIT AND TOTAL CREDIT SHOULD BE EQUAL
            double totalSum = 0;
            for(PostJSON postJSON : verJSON.getDebitposts()) {
                totalSum = totalSum + postJSON.getSum();
            }
            verJSON.setSum(totalSum);
            
            verJSONLs.add(verJSON);
        }
        
        return verJSONLs;
    }
}
