
package se.chalmers.bokforing.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.service.CustomerManager;
import se.chalmers.bokforing.service.CustomerService;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.service.VerificationManager;
import se.chalmers.bokforing.service.AccountService;
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
    AccountService accountService;
    
    @Autowired 
    private AuthSession authSession;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomerManager customerManager;
    
    @Autowired
    private CustomerService customerService;
    
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
            temp_post.setSum(temp_postSum);
            
            new_posts.add(temp_post);
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER; CREATE VERIFICATION
        UserAccount user = userService.getUser(email);
        //userService.storeUser(user);
        
        // TODO
        long customerNumber = 123;
        Customer customer = customerManager.createCustomer(user, customerNumber, "Dzeno", "00387", null);
        Customer customerFromDb = customerService.findByCustomerNumber(user, customerNumber);
        
        // CREATE VERIFICATION
        Verification ver = verificationManager.createVerification(user, new_posts, verification.getTransactionDate(), customerFromDb);
        user.getVerifications().add(ver);
        userService.storeUser(user);
        
        // System.out.println(userService.getUser(authSession.getEmail()).getVerifications().get(0));
        
        return form;
    }
    
    @RequestMapping(value = "/bookkeeping/searchaccount", method = RequestMethod.POST)
    public @ResponseBody List<Account> searchaccount(@RequestBody final AccountJSON account) {
        List<Account> accLs;
        
        if(account.getNumber() > 0) {
            accLs = accountService.findByNumberLike(account.getNumber());
        } else if (!(account.getName().isEmpty())) {
            accLs = accountService.findByNameLike(account.getName());
        } else {
            // Return a empty list
            accLs = new ArrayList();
        }
        return accLs;
    }
}
