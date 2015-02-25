
package se.chalmers.bokforing.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.chalmers.bokforing.helperfunctions.HelpY;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.UserJSON;
import se.chalmers.bokforing.persistence.UserDb;
import se.chalmers.bokforing.persistence.UserEnt;
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
    private UserDb userDb;
    
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
        UserEnt userEnt= userDb.getUser(user.getEmail());
        if(userEnt == null) {
            form.addError("email", "E-post adressen existerar inte!");
            return form;
        }
        
        // PASSWORD CHECK
        if(user.getPasswd() == null || user.getPasswd().isEmpty()) {
            form.addError("passwd", "Du har inte angett något lösenord!");
            return form;
        }
        String hashPasswd = hash(user.getPasswd());
        if(!hashPasswd.equals(userEnt.getPass())) {
            form.addError("passwd", "Lösenordet är fel!");
            return form;
        }
        
        /* LOGIN SUCCESSFUL
         * Store user in session 
         */
        authSession.setSession(userEnt.getEmail(), helpy.randomString(10), userEnt.getGroup().toString());
        return form;
    }
    
    /*
     * STATUS
     * Check if the user is online or offline
     */
    @RequestMapping(value = "/auth/status", method = RequestMethod.GET)
    public @ResponseBody boolean status() {
        System.out.println("* PING auth/status");

        return authSession.getStatus();
    }
    
    /*
     * GET
     */
    @RequestMapping(value = "/auth/get", method = RequestMethod.GET)
    public @ResponseBody UserJSON get() {
        System.out.println("* PING auth/get");

        if(authSession.getStatus()) {
            return new UserJSON(authSession.getEmail(), 
                    authSession.getSessionid(), authSession.getLevel());
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
    
    private String hash(String plaintext) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plaintext.getBytes());
            return new String(hash);
        } catch(NoSuchAlgorithmException e) {
            return "";
        }        
    }
}
