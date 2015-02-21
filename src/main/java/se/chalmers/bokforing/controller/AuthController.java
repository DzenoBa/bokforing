
package se.chalmers.bokforing.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.UserJSON;
import se.chalmers.bokforing.persistence.UserEnt;
import se.chalmers.bokforing.persistence.UserRepository;
//import se.chalmers.bokforing.session.AuthSession;

/**
 * AUTH-CONTROLLER
 * Handles JSON requests
 * 
 * @author Dženan
 */
@Controller
public class AuthController {
    
    // TODO
    //@Autowired private AuthSession authSession;
    
    @Autowired
    private UserRepository userRepo;
    
    /*
     * LOGIN
     */
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public @ResponseBody FormJSON login(HttpSession session, @RequestBody final UserJSON user) {
        System.out.println("* PING auth/login");
        FormJSON form = new FormJSON();
        
        // USERNAME CHECK
        if(user.getUsername() == null || user.getUsername().isEmpty()) {
            form.addError("username", "Du har inte angett något användarnamn!");
            return form;
        }
        List<UserEnt> userEntLs = userRepo.findByName(user.getUsername());
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
        session.setAttribute("user", new UserJSON(userEnt.getName(), "sesid", userEnt.getGroup2()));
        return form;
    }
    
    /*
     * STATUS
     * Check if the user is online or offline
     */
    @RequestMapping(value = "/auth/status", method = RequestMethod.GET)
    public @ResponseBody boolean status(HttpSession session) {
        System.out.println("* PING auth/status");

        if(session.getAttribute("user") != null) {
            return true;
        } else {
            return false;
        }
    }
    
    /*
     * GET
     */
    @RequestMapping(value = "/auth/get", method = RequestMethod.GET)
    public @ResponseBody UserJSON get(HttpSession session) {
        System.out.println("* PING auth/get");

        if(session.getAttribute("user") != null) {
            return (UserJSON) session.getAttribute("user");
        } else {
            return null;
        }
    }
    
    /*
     * LOGOUT
     */
    @RequestMapping(value = "/auth/logout", method = RequestMethod.GET) // GET?
    public @ResponseBody boolean logout(HttpSession session) {
        System.out.println("* PING auth/logout");
        // REMOVE USER FROM SESSION
        session.removeAttribute("user");
        return true;
    }
}
