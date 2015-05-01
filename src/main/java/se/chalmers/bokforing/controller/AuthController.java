
package se.chalmers.bokforing.controller;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.chalmers.bokforing.util.PasswordUtil;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.UserJSON;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.service.UserService;
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
    
    /*
     * LOGIN
     */
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public @ResponseBody FormJSON login(@RequestBody final UserJSON user) {
        System.out.println("* PING auth/login");
        FormJSON form = new FormJSON();
        
        // EMAIL CHECK
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            form.addError("email", "Du har inte angett någon e-postadress.");
            return form;
        }
        // CHECK IF VALID EMAIL
        String regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern patternEmail = Pattern.compile(regexEmail);
        if(!(patternEmail.matcher(user.getEmail()).find())) {
            form.addError("email", "Vänligen ange en korrekt e-postadress på formatet exempel@exempel.com.");
            return form;
        }
        // CHECK IF EMAIL EXIST
        UserHandler userEnt= userDb.getUser(user.getEmail());
        if(userEnt == null) {
            form.addError("email", "E-postadressen existerar inte.");
            return form;
        }
        
        // PASSWORD CHECK
        if(user.getPasswd() == null || user.getPasswd().isEmpty()) {
            form.addError("passwd", "Du har inte angett något lösenord.");
            return form;
        }
        //CONCAT SALT + PASSWD; THEN HASH IT
        String hashPasswd = PasswordUtil.hash(userEnt.getSalt() + user.getPasswd());
        if(!hashPasswd.equals(userEnt.getPass())) {
            form.addError("passwd", "Lösenordet är fel.");
            return form;
        }
        
        /* LOGIN SUCCESSFUL
         * Store user in session 
         */
        String s_id = PasswordUtil.randomString(10);
        authSession.setSession(userEnt.getEmail(), s_id, userEnt.getUserGroup().toString());
        //Store session and timestamp in database
        userEnt.setSessionid(s_id);
        userEnt.setLastLogIn();
        userDb.storeUser(userEnt);
        
        return form;
    }
        
    /*
     * GET AUTHENTICATION
     */
    @RequestMapping(value = "/auth/getauthentication", method = RequestMethod.GET)
    public @ResponseBody UserJSON getauthentication() {
        System.out.println("* PING auth/get");
        UserJSON uJSON = new UserJSON();
        if(sessionCheck()) {
            uJSON.setEmail(authSession.getEmail());
            uJSON.setLevel(authSession.getLevel());
            return uJSON;
        } else {
            return uJSON;
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
            UserHandler u = userDb.getUser(authSession.getEmail());
            
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
