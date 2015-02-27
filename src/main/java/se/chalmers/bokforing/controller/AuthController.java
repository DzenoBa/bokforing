
package se.chalmers.bokforing.controller;

import java.util.Date;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.chalmers.bokforing.helperfunctions.HelpY;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.UserJSON;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.session.AuthSession;

/**
 * AUTH-CONTROLLER
 * Handles JSON requests
 * 
 * @author Dženan
 */
@Controller
public class AuthController {
    
    @Autowired 
    private AuthSession authSession;
    
    @Autowired
    private UserService userDb;
    
    private HelpY helpy = new HelpY();
    
    /*
     * LOGIN
     */
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public @ResponseBody FormJSON login(@RequestBody final UserJSON user) {
        System.out.println("* PING auth/login");
        FormJSON form = new FormJSON();
        
        // EMAIL CHECK
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            form.addError("email", "Du har inte angett någon e-post adress!");
            return form;
        }
        // CHECK IF VALID EMAIL
        String regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern patternEmail = Pattern.compile(regexEmail);
        if(!(patternEmail.matcher(user.getEmail()).find())) {
            form.addError("email", "Vänligen ange en e-post adress!");
            return form;
        }
        // CHECK IF EMAIL EXIST
        UserAccount userEnt= userDb.getUser(user.getEmail());
        if(userEnt == null) {
            form.addError("email", "E-post adressen existerar inte!");
            return form;
        }
        
        // PASSWORD CHECK
        if(user.getPasswd() == null || user.getPasswd().isEmpty()) {
            form.addError("passwd", "Du har inte angett något lösenord!");
            return form;
        }
        //CONCAT SALT + PASSWD; THEN HASH IT
        String hashPasswd = helpy.hash(userEnt.getSalt() + user.getPasswd());
        if(!hashPasswd.equals(userEnt.getPass())) {
            form.addError("passwd", "Lösenordet är fel!");
            return form;
        }
        
        /* LOGIN SUCCESSFUL
         * Store user in session 
         */
        String s_id = helpy.randomString(10);
        authSession.setSession(userEnt.getEmail(), s_id, userEnt.getGroup().toString());
        //Store session and timestamp in database
        userEnt.setSessionid(s_id);
        userEnt.setLastLogIn(new Date());
        userDb.storeUser(userEnt);
        
        return form;
    }
    
    /*
     * STATUS
     * Check if the user is online or offline
     */
    @RequestMapping(value = "/auth/status", method = RequestMethod.GET)
    public @ResponseBody boolean status() {
        System.out.println("* PING auth/status");

        return sessionCheck();
    }
    
    /*
     * GET
     */
    @RequestMapping(value = "/auth/get", method = RequestMethod.GET)
    public @ResponseBody UserJSON get() {
        System.out.println("* PING auth/get");

        if(sessionCheck()) {
            UserJSON uJSON = new UserJSON();
            uJSON.setEmail(authSession.getEmail());
            uJSON.setLevel(authSession.getLevel());
            return uJSON;
        } else {
            return null;
        }
    }
    
    /*
     * LOGOUT
     */
    @RequestMapping(value = "/auth/logout", method = RequestMethod.GET) // GET?
    public @ResponseBody boolean logout() {
        System.out.println("* PING auth/logout");
        // REMOVE USER FROM SESSION
        authSession.clearSession();
        return true;
    }
    
    private boolean sessionCheck() {
        
        // CHECK IF USER IS ONLINE
        if(authSession.getStatus()) {
            UserAccount u = userDb.getUser(authSession.getEmail());
            
            // CHECK IF USER EXIST
            if(u == null) {
                authSession.clearSession();
                return false;
            }
            
            // CHECK IF THE SESSION IS CORRECT
            if(!(u.getSessionid().equals(authSession.getSessionid()))) {
                authSession.clearSession();
                return false;
            }
            
            // EVERYTHING SEEMS TO BE IN ORDER
            return true;
        }
        return false;
    }
    
}
