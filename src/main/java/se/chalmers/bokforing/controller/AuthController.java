
package se.chalmers.bokforing.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.UserJSON;
import se.chalmers.bokforing.persistence.UserDb;
import se.chalmers.bokforing.persistence.UserEnt;
import se.chalmers.bokforing.persistence.UserRepository;
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
    private UserRepository userRep;
    
    /*
     * LOGIN
     */
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public @ResponseBody FormJSON login(@RequestBody final UserJSON user) {
        System.out.println("* PING auth/login");
        FormJSON form = new FormJSON();
        
        // USERNAME CHECK
        if(user.getUsername() == null || user.getUsername().isEmpty()) {
            form.addError("username", "Du har inte angett något användarnamn!");
            return form;
        }
        List<UserEnt> userEntLs = UserDb.getUsersByName(userRep,user.getUsername());
        if(userEntLs == null || userEntLs.isEmpty()) {
            form.addError("username", "Användarnamnet existerar inte!");
            return form;
        }
        
        // GET USER FROM LIST
        UserEnt userEnt = userEntLs.get(0);
        
        // PASSWORD CHECK
        if(user.getPasswd() == null || user.getPasswd().isEmpty()) {
            form.addError("passwd", "Du har inte angett något lösenord!");
            return form;
        }
        else if(!user.getPasswd().equals(userEnt.getPass())) {
            form.addError("passwd", "Lösenordet är fel!");
            return form;
        }
        
        /* LOGIN SUCCESSFUL
         * Store user in session 
         */
        authSession.setSession(userEnt.getName(), "randomSesId", userEnt.getGroup().toString());
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
            return new UserJSON(authSession.getUsername(), 
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
}
