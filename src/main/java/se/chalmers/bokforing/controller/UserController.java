
package se.chalmers.bokforing.controller;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.util.PasswordUtil;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.UserJSON;
import se.chalmers.bokforing.model.UserGroup;
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
    
    PasswordUtil helpy = new PasswordUtil();
    
    /*
     * CREATE
     */
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public @ResponseBody FormJSON create(@RequestBody final UserJSON user) {
        System.out.println("* PING user/create");
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
        UserAccount userAccExist = userDb.getUser(user.getEmail());
        if(userAccExist != null) {
            form.addError("email", "E-post adressen är redan registrerad!");
            return form;
        }
        
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
        
        // EVERYTHING SEEMS TO BE IN ORDER CREATE USER
        UserAccount userAcc = new UserAccount();
        userAcc.setEmail(user.getEmail());
        String salt = helpy.randomString(8);
        String hashPasswd = helpy.hash(salt + user.getNewpasswd());
        userAcc.setSalt(salt);
        userAcc.setPass(hashPasswd);
        userAcc.setGroup(UserGroup.User);
        // STORE
        userDb.storeUser(userAcc);
            
        return form;
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
        String newHashPasswd = helpy.hash(newSalt + user.getNewpasswd());
        u.setSalt(newSalt);
        u.setPass(newHashPasswd);
        userDb.storeUser(u);
        
        return form;
    }
}
