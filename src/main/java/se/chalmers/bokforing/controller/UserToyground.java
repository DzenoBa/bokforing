/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.controller;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.util.PasswordUtil;

/**
 *
 * @author victor
 */
@Controller
public class UserToyground {

    @Autowired
    private UserService userDb;
        
    /**
     * Request mapping for user
     *
     * @return
     */
    private StringBuilder test2(String email){
        StringBuilder sb = new StringBuilder();
        UserHandler uh = userDb.getUser(email);
        sb.append("changing pass from ").append(uh.getPass());
        String str = "bananas";
        sb.append(" into ").append(str);
        uh.setPass(str);
        userDb.storeUser(uh);
        return sb;
    }
    
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView getUsersView() {

        String email = "whoop";
        UserHandler uh = new UserHandler();

        uh.setEmail(email);
        uh.setPass(PasswordUtil.randomString(8));
        StringBuilder sb = new StringBuilder();
        try {
            userDb.storeUser(uh);
        } catch (javax.persistence.PersistenceException e) {
            sb.append("Error: user is already in db. ");
            //Goto phase 2
            sb.append(test2(email));
        }

        UserHandler ua = userDb.getUser("whoop");
        if (ua != null) {
            sb.append(ua.toString());
        } else {
            sb.append("null");
        }

        return new ModelAndView("test", "message", sb.toString());
    }
}

