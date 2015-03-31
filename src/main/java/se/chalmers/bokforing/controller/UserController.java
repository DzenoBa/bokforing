
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
import se.chalmers.bokforing.jsonobject.UserInfoJSON;
import se.chalmers.bokforing.jsonobject.UserJSON;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.user.UserGroup;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.model.user.UserInfo;
import se.chalmers.bokforing.service.UserManager;
import se.chalmers.bokforing.persistence.user.UserService;
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
    private UserService userDb;
    
    @Autowired 
    private AuthSession authSession;
    
    @Autowired 
    private UserManager userManager;
    
    @Autowired
    private UserService userService;
    
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
        UserHandler userAccExist = userDb.getUser(user.getEmail());
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
        UserHandler uh = new UserHandler();
        uh.setEmail(user.getEmail());
        String hashPasswd = PasswordUtil.hash(uh.getSalt() + user.getNewpasswd());
        uh.setPass(hashPasswd);
        // STORE
        userManager.createUser(uh);
            
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
        UserHandler uh =userDb.getUser(email);
        if(uh == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund!");
            return form;
        }
        
        // CHECK IF VALID PASSWORD
        if(user.getPasswd() == null || user.getPasswd().isEmpty()) {
            form.addError("passwd", "Ange ditt nuvarande lösenord!");
            return form;
        }
        String hashPasswd = PasswordUtil.hash(uh.getSalt() + user.getPasswd());
        if(!(hashPasswd.equals(uh.getPass()))) {
            form.addError("passwd", "Löseordet är fel!");
            return form;
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER CHANGE USER
        String newHashPasswd = PasswordUtil.hash(uh.getSalt() + user.getNewpasswd());
        uh.setPass(newHashPasswd);
        userDb.storeUser(uh);
        
        return form;
    }
    
    @RequestMapping(value = "/user/getuserinfo", method = RequestMethod.GET)
    public @ResponseBody UserInfoJSON getUserInfo() {
        
        UserInfoJSON userInfo = new UserInfoJSON();
        // CHECK SESSION
        if(!(authSession.sessionCheck())) {
            return userInfo;
        }
        String email = authSession.getEmail();
        
        UserHandler ua = userService.getUser(email);
        
        // SET
        userInfo.setFirstname(ua.getName());
        userInfo.setPhonenumber(ua.getPhoneNumber());
        userInfo.setCompanyname(ua.getCompanyName());
        
        return userInfo;
    }
    
    @RequestMapping(value = "/user/edituserinfo", method = RequestMethod.PUT)
    public @ResponseBody FormJSON editUserInfo(@RequestBody final UserInfoJSON userInfo) {
        
        System.out.println("* PING user/editUserInfo");
        FormJSON form = new FormJSON();
        
        if(userInfo == null) {
            form.addError("general", "Ett fel inträffades, vänligen försök igen om en liten stund!");
            return form;
        }
        
        // CHECK SESSION
        if(!(authSession.sessionCheck())) {
            form.addError("general", "Ett fel inträffades, du har inte rätt tillstånd för att utföra denna åtgärd!");
            return form;
        }
        String email = authSession.getEmail();
        UserHandler uh = userService.getUser(email);

        
        // USER REQUESTED TO CHANGE FIRSNAME
        if(userInfo.getFirstname() != null) {
            uh.setName(userInfo.getFirstname());
            userDb.storeUser(uh);
            return form;
        }
        
        // USER REQUESTED TO CHANGE LASTNAME
        if(userInfo.getLastname() != null) {
            form.addError("general", "Ej implementerad"); // TODO
            return form;
        }
        
        // USER REQUESTED TO CHANGE PHONE-NUMBER
        if(userInfo.getPhonenumber() != null) {
            uh.setPhoneNumber(userInfo.getPhonenumber());
            userDb.storeUser(uh);
            return form;
        }
        
        // USER REQUESTED TO CHANGE COMPANY NAME
        if(userInfo.getCompanyname() != null) {
            uh.setPhoneNumber(userInfo.getCompanyname());
            userDb.storeUser(uh);
            return form;
        }
        
        // YOU SHOULD NOT BE ABLE TO REACH THIS PART
        form.addError("general", "Någon gick fel, vänligen försök igen om en liten stund");
        return form;
    }
}
