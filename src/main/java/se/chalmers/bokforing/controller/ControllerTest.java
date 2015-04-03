package se.chalmers.bokforing.controller;

import java.util.ArrayList;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import se.chalmers.bokforing.util.PasswordUtil;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.model.PostType;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.persistence.user.UserService;
import se.chalmers.bokforing.service.VerificationManager;
import se.chalmers.bokforing.service.VerificationService;

@Controller
public class ControllerTest {

    @Autowired
    private UserService userDb;

    @Autowired
    private VerificationManager verManager;

    @Autowired
    private VerificationService verService;

    @RequestMapping("/test2")
    public ModelAndView getNewPage() {
        Calendar cal = Calendar.getInstance();
        Account account = new Account();
        account.setName("Egna insättningar");
        //account.setNumber("2018");

        PostSum sum = new PostSum();
        sum.setSumTotal(100);
        sum.setType(PostType.Credit);

        Post post = new Post();
        //post.setPostDate(cal.getTime());
        post.setPostSum(sum);
        post.setAccount(account);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);

        String verNbr = "123";
        //verManager.createVerification(verNbr, postList);
        //Verification verification = verService.findVerificationById(verNbr);

        return new ModelAndView("test3", "message", /*verification.toString()*/ "no");
    }
}
