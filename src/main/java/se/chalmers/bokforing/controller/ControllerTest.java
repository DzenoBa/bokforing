package se.chalmers.bokforing.controller;

import java.util.ArrayList;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import se.chalmers.bokforing.helperfunctions.HelpY;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.model.PostType;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.model.UserInfo;
import se.chalmers.bokforing.persistence.UserInfoRepository;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.service.VerificationManager;
import se.chalmers.bokforing.service.VerificationService;

@Controller
public class ControllerTest {

    @Autowired
    private UserService userDb;

    @Autowired
    private UserInfoRepository userInfoRep;

    @Autowired
    private VerificationManager verManager;

    @Autowired
    private VerificationService verService;

    /**
     * Request mapping for user
     *
     * @return
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView getUsersView() {
        UserAccount user = new UserAccount();

        UserInfo ui = new UserInfo();
        ui.setName("victor");
        ui.setCompanyName("whoops");
        ui.setPhoneNumber("0123456789");
        userInfoRep.save(ui);

        String email = "whoop";
        user.setEmail(email);
        user.setPass(HelpY.randomString(8));
        user.setUserInfo(ui);
        StringBuilder sb = new StringBuilder();
        sb.append("UserInfo " + ui.getUserInfoId() + " was added.\n");
        try {
            userDb.storeUser(user);
        } catch (Exception e) {
            sb.append("Error: user is already in db. ");
        }

        UserAccount ua = userDb.getUser("whoop");
        if (ua != null) {
            sb.append(ua.toString());
        } else {
            sb.append("null");
        }

        return new ModelAndView("test", "message", sb.toString());
    }

    @RequestMapping("/test2")
    public ModelAndView getNewPage() {
        Calendar cal = Calendar.getInstance();
        Account account = new Account();
        account.setName("Egna insättningar");
        account.setNumber("2018");

        PostSum sum = new PostSum();
        sum.setSumTotal(100);
        sum.setType(PostType.Credit);

        Post post = new Post();
        post.setPostDate(cal.getTime());
        post.setSum(sum);
        post.setAccount(account);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);

        String verNbr = "123";
        verManager.createVerification(verNbr, postList);
        Verification verification = verService.findVerificationById(verNbr);

        return new ModelAndView("test3", "message", verification.toString());
    }

}
