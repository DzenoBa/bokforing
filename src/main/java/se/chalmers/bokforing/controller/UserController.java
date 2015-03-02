
package se.chalmers.bokforing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.helperfunctions.HelpY;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.UserJSON;
import se.chalmers.bokforing.model.Group;
import se.chalmers.bokforing.service.UserServiceImpl;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.session.AuthSession;

/**
 * USER-CONTROLLER
 * Handles JSON requests
 * 
 * @author Dženan
 */
@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userDb;
    
    @Autowired 
    private AuthSession authSession;
    
    HelpY helpy = new HelpY();
    
    /*
     * SET
     */
    @RequestMapping(value = "/user/set", method = RequestMethod.GET)
    public @ResponseBody FormJSON set() {
        System.out.println("* PING user/get");

        FormJSON form = new FormJSON();

        UserAccount userEnt = userDb.getUser("dzeno@bazdar.ba");
        
        if(userEnt == null) {
            // CREATE A NEW USER
            UserAccount u = new UserAccount();
            u.setEmail("dzeno@bazdar.ba");
            // CREATE A RANDOM SALT
            String salt = helpy.randomString(8);
            u.setSalt(salt);
            u.setPass(salt + "passwd");
            u.setGroup(Group.Admin);
            userDb.storeUser(u);
            
            return form;
        }
        // USER ALREADY EXIST
        else {
            form.addError("create", "E-post adressen 'dzeno@bazdar.ba' finns redan i databasen!");
            
            return form;
        }
    }
    
    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public @ResponseBody FormJSON edit(@RequestBody final UserJSON user) {
        System.out.println("* PING user/edit");
        FormJSON form = new FormJSON();
        
        // PASSWORD CHECK
        if(user.getNewpasswd() == null || user.getNewpasswd().isEmpty()) {
            form.addError("newpasswd", "Vänligen ange ett lösenord!");
            return form;
        }
        if(user.getNewpasswd2() == null || user.getNewpasswd2().isEmpty()) {
            form.addError("newpasswd2", "Vänligen ange ett lösenord!");
            return form;
        }
        if(!(user.getNewpasswd().equals(user.getNewpasswd2()))) {
            form.addError("newpasswd2", "Lösenordet matchar inte!");
            return form;
        }
        
        String email = authSession.getEmail();
        if(email == null || email.isEmpty()) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund!");
            return form;
        }
        UserAccount u = userDb.getUser(email);
        if(u == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund!");
            return form;
        }
        
        // CHECK IF VALID PASSWORD
        if(user.getPasswd() == null || user.getPasswd().isEmpty()) {
            form.addError("passwd", "Ange ditt nuvarande lösenord!");
            return form;
        }
        String hashPasswd = helpy.hash(u.getSalt() + user.getPasswd());
        if(!(hashPasswd.equals(u.getPass()))) {
            form.addError("passwd", "Löseordet är fel!");
            return form;
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER CHANGE USER
        String newSalt = helpy.randomString(8);
        String newHashPasswd = newSalt + user.getNewpasswd(); // TODO; hash it here or in UserDB
        u.setSalt(newSalt);
        u.setPass(newHashPasswd);
        userDb.storeUser(u);
        
        return form;
    }
}
