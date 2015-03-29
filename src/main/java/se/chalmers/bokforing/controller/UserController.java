
package se.chalmers.bokforing.controller;

import java.util.Calendar;
import java.util.Date;
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
import se.chalmers.bokforing.model.AccessKey;
import se.chalmers.bokforing.model.AccessKeyType;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.user.UserGroup;
import se.chalmers.bokforing.model.user.UserInfo;
import se.chalmers.bokforing.service.UserManager;
import se.chalmers.bokforing.persistence.user.UserService;
import se.chalmers.bokforing.service.AccessKeyManager;
import se.chalmers.bokforing.service.AccessKeyService;
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
    
    @Autowired
    private AccessKeyService accessKeyService;
    
    @Autowired
    private AccessKeyManager accessKeyManager;
    
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
        String hashPasswd = PasswordUtil.hash(userAcc.getSalt() + user.getNewpasswd());
        userAcc.setPass(hashPasswd);
        userAcc.setUserGroup(UserGroup.User);
        // STORE
        userManager.createUser(userAcc);
            
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
        String hashPasswd = PasswordUtil.hash(u.getSalt() + user.getPasswd());
        if(!(hashPasswd.equals(u.getPass()))) {
            form.addError("passwd", "Löseordet är fel!");
            return form;
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER CHANGE USER
        String newHashPasswd = PasswordUtil.hash(u.getSalt() + user.getNewpasswd());
        u.setPass(newHashPasswd);
        userDb.storeUser(u);
        
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
        
        UserAccount ua = userService.getUser(email);
        UserInfo ui = ua.getUseInfo();
        
        if(ui == null) {
            return userInfo;
        }
        
        // SET
        userInfo.setFirstname(ui.getName());
        userInfo.setPhonenumber(ui.getPhoneNumber());
        userInfo.setCompanyname(ui.getCompanyName());
        
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

        
        // USER REQUESTED TO CHANGE FIRSNAME
        if(userInfo.getFirstname() != null) {
            userService.updateName(userInfo.getFirstname(), email);
            return form;
        }
        
        // USER REQUESTED TO CHANGE LASTNAME
        if(userInfo.getLastname() != null) {
            form.addError("general", "Ej implementerad"); // TODO
            return form;
        }
        
        // USER REQUESTED TO CHANGE PHONE-NUMBER
        if(userInfo.getPhonenumber() != null) {
            userService.updatePhoneNumber(userInfo.getPhonenumber(), email);
            return form;
        }
        
        // USER REQUESTED TO CHANGE COMPANY NAME
        if(userInfo.getCompanyname() != null) {
            userService.updateCompanyName(userInfo.getCompanyname(), email);
            return form;
        }
        
        // YOU SHOULD NOT BE ABLE TO REACH THIS PART
        form.addError("general", "Någon gick fel, vänligen försök igen om en liten stund");
        return form;
    }
    
    /**
     * REQUEST KEY FOR PASSWORD RESET
     * @param user
     * @return 
     */
    @RequestMapping(value = "/user/passwdreset", method = RequestMethod.POST)
    public @ResponseBody FormJSON requestKeyPasswdReset(@RequestBody final UserJSON user) {
        
        System.out.println("* PING accesskey/forgotpasswd");
        FormJSON form = new FormJSON();
        
        if(user.getEmail() == null) {
            form.addError("general", "Vänligen ange en e-post adress.");
            return form;
        }
        // CHECK IF VALID EMAIL
        String regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern patternEmail = Pattern.compile(regexEmail);
        if(!(patternEmail.matcher(user.getEmail()).find())) {
            form.addError("general", "Vänligen ange en e-post adress!");
            return form;
        }
        
        // CHECK IF EMAIL EXIST
        UserAccount userAccount = userService.getUser(user.getEmail());
        if(userAccount == null) {
            form.addError("general", "Det finns inget konto registrerat med den angivna e-post adressen.");
            return form;
        }
        
        // CHECK IF USER ALREADE REQUESTED A KEY
        AccessKey freshKey = accessKeyService.findByUserAccountAndType(userAccount, AccessKeyType.FORGOTPASSWD);
        if(freshKey != null) {
            Date todaysDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(todaysDate);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            // PREVENT USER GENERATING A NEW KEY
            // IF THE DIFFERENCE BETWEEN NOW-TIME AND PREVIOUS KEYS DATE
            // IS LESSER THAN 20 MIN
            if((freshKey.getCreationDate().getTime() - cal.getTimeInMillis()) <= 20*60*1000) {
                form.addError("general", "Du har redan skickat en nyckel till din e-post adress!");
                return form;
            }
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER; CREATE KEY
        String randomKey = PasswordUtil.randomString(15);
        String hashedKey = randomKey; // TODO
        AccessKey newAccessKey = accessKeyManager.create(hashedKey, AccessKeyType.FORGOTPASSWD, userAccount);
        if(newAccessKey == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund");
            return form;
        }
        
        // EMAIL THE USER
        // TO-DO
        
        return form;
    }
    
    /**
     * PASSWORD RESET KEY EXIST
     * @param user
     * @return String
     */
    @RequestMapping(value = "/user/keyexist", method = RequestMethod.POST)
    public @ResponseBody UserJSON passwdResetKeyExist(@RequestBody final UserJSON user) {
        
        UserJSON userJSON = new UserJSON();
        
        if(user.getAccesskey() == null) {
            return userJSON;
        }
        // CHECK KEY
        AccessKey accessKey = accessKeyService.findByKey(user.getAccesskey());
        if(accessKey == null) {
            return userJSON;
        } else if(accessKey.getType().equals(AccessKeyType.FORGOTPASSWD)) {
            // KEY EXIST
            userJSON.setEmail(accessKey.getUserAccount().getEmail());
            return userJSON;
        }

        return userJSON;
    }
    
    @RequestMapping(value = "/user/passwdrecovery", method = RequestMethod.POST)
    public @ResponseBody FormJSON passwdRecovery(@RequestBody final UserJSON user) {
        
        FormJSON form = new FormJSON();
        
        // GET USER
        UserAccount userAccount = userService.getUser(user.getEmail());
        if(userAccount == null) {
            form.addError("general", "Något gick fel, vängligen försök igen om en liten stund");
            return form;
        }
        
        // GET KEY
        AccessKey accessKey = accessKeyService.findByUserAccountAndType(userAccount, AccessKeyType.FORGOTPASSWD);
        if(accessKey == null) {
            form.addError("general", "Något gick fel, vängligen försök igen om en liten stund");
            return form;
        } else if(!accessKey.getKey().equals(user.getAccesskey())) {
            form.addError("general", "Något gick fel, vängligen försök igen om en liten stund");
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

        // EVERYTHING SEEMS TO BE IN ORDER CHANGE PASSWORD
        String newHashPasswd = PasswordUtil.hash(userAccount.getSalt() + user.getNewpasswd()); //TO DO
        userAccount.setPass(newHashPasswd);
        userDb.storeUser(userAccount);
        
        // DELETE ACCESS KEY
        accessKeyService.removeByUserAccount(userAccount);
        
        return form;
    }
    
    
}
