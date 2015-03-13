
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
        
        if(verification.getPosts() == null || verification.getPosts().isEmpty()) { // TODO sessionCheck
            form.addError("general", "Ett fel inträffades vänligen försök igenom om en liten stund");
            return form;
        }
        
        if(verification.getTransactionDate() == null) {
            form.addError("verificationdate", "Ange ett datum");
            return form;
        }
        
        // TODO ACCOUNT
        Account temp_account = new Account();
        temp_account.setName("TEST");
        temp_account.setNumber(123456);
            
        // CREATE POSTS
        List<Post> new_posts = new ArrayList();
        for(PostJSON post : verification.getPosts()) {
            if(post == null) {
                form.addError("todo", "Ett fel inträffades, vänligen försök igen om en liten stund. Code: P01"); // TODO
                return form;
            }
            if(post.getDebit() < 0 || post.getCredit() < 0) {
                form.addError("todo", "Ett fel inträffades, vänligen försök igen om en liten stund. Code: P02"); // TODO
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
                form.addError("todo", "Ett fel inträffades, vänligen försök igen om en liten stund. Code: DC01"); // TODO
            }
            
            // EVERYTHING SEEMS TO BE IN ORDER; CREATE POST
            Post temp_post = new Post();
            temp_post.setAccount(temp_account);
            temp_post.setSum(temp_postSum);
            
            new_posts.add(temp_post);
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER; CREATE VERIFICATION
        UserAccount user = userService.getUser(authSession.getEmail());
        //userService.storeUser(user);
        
        long customerNumber = 123;
        Customer customer = customerManager.createCustomer(user, customerNumber, "Dzeno", "00387", null);
        Customer customerFromDb = customerService.findByCustomerNumber(user, customerNumber);
        
        Verification ver = verificationManager.createVerification(user, new_posts, verification.getTransactionDate(), customerFromDb); // TODO
        user.getVerifications().add(ver);
        userService.storeUser(user);
        
        if(ver == null) {
            form.addError("general", "Ver. error");
        }
        
        System.out.println(userService.getUser(authSession.getEmail()).getVerifications().get(0));
        
        return form;
    }
}
