
package se.chalmers.bokforing.controller;

import javax.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.UserJSON;
//import se.chalmers.bokforing.session.AuthSession;

/**
 * AUTH-CONTROLLER
 * Handles JSON requests
 * 
 * @author Dženan
 */
@Controller
@RequestMapping("auth")
//@ComponentScan("se.chalmers.bokforing")
public class AuthCtrl {
    
    // TODO
    //@Autowired private AuthSession authSession;
    
    /*
     * LOGIN
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody FormJSON login(HttpSession session, @RequestBody final UserJSON user) {
        System.out.println("* PING auth/login");
        FormJSON form = new FormJSON();
        
        // USERNAME CHECK
        if(user.getUsername() == null || user.getUsername().isEmpty()) {
            form.addError("username", "Du har inte angett något användarnamn!");
            return form;
        }
        else if(!user.getUsername().equals("user")) {
            form.addError("username", "Användarnamnet existerar inte!");
            return form;
        }
        
        // PASSWORD CHECK
        if(user.getPasswd() == null || user.getPasswd().isEmpty()) {
            form.addError("passwd", "Du har inte angett något lösenord!");
            return form;
        }
        else if(!user.getPasswd().equals("passwd")) {
            form.addError("passwd", "Lösenorder är fel!");
            return form;
        }
        
        /* LOGIN SUCCESSFUL
         * Store user in session 
         */
        session.setAttribute("user", new UserJSON(user.getUsername(), "sesid", "admin"));
        return form;
    }
    
    /*
     * STATUS
     * Check if the user is online or offline
     */
    @RequestMapping(value = "/status", method = RequestMethod.GET)
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
    @RequestMapping(value = "/get", method = RequestMethod.GET)
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
    @RequestMapping(value = "/logout", method = RequestMethod.GET) // GET?
    public @ResponseBody boolean logout(HttpSession session) {
        System.out.println("* PING auth/logout");
        // REMOVE USER FROM SESSION
        session.removeAttribute("user");
        return true;
    }
}
