package se.chalmers.bokforing.controller;

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
import se.chalmers.bokforing.model.Address;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.service.UserManager;
import se.chalmers.bokforing.service.AccessKeyManager;
import se.chalmers.bokforing.service.AccessKeyService;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.session.AuthSession;

/**
 * USER-CONTROLLER Handles JSON requests
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
    public @ResponseBody
    FormJSON create(@RequestBody final UserJSON user) {
        System.out.println("* PING user/create");
        FormJSON form = new FormJSON();

        // EMAIL CHECK
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            form.addError("email", "Du har inte angett någon e-postadress.");
            return form;
        }
        // CHECK IF VALID EMAIL
        String regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern patternEmail = Pattern.compile(regexEmail);
        if (!(patternEmail.matcher(user.getEmail()).find())) {
            form.addError("email", "Vänligen ange en korrekt e-postadress på formatet exempel@exempel.com");
            return form;
        }
        // CHECK IF EMAIL EXIST
        UserHandler userAccExist = userDb.getUser(user.getEmail());
        if (userAccExist != null) {
            form.addError("email", "E-postadressen är redan registrerad.");
            return form;
        }

        // PASSWORD CHECK
        if (user.getNewpasswd() == null || user.getNewpasswd().isEmpty()) {
            form.addError("newpasswd", "Vänligen ange ett lösenord.");
            return form;
        }
        if (user.getNewpasswd2() == null || user.getNewpasswd2().isEmpty()) {
            form.addError("newpasswd2", "Vänligen ange ett lösenord.");
            return form;
        }
        if (!(user.getNewpasswd().equals(user.getNewpasswd2()))) {
            form.addError("newpasswd2", "Lösenorden matchar inte.");
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
    public @ResponseBody
    FormJSON edit(@RequestBody final UserJSON user) {
        System.out.println("* PING user/edit");
        FormJSON form = new FormJSON();

        // PASSWORD CHECK
        if (user.getNewpasswd() == null || user.getNewpasswd().isEmpty()) {
            form.addError("newpasswd", "Vänligen ange ett lösenord.");
            return form;
        }
        if (user.getNewpasswd2() == null || user.getNewpasswd2().isEmpty()) {
            form.addError("newpasswd2", "Vänligen ange ett lösenord.");
            return form;
        }
        if (!(user.getNewpasswd().equals(user.getNewpasswd2()))) {
            form.addError("newpasswd2", "Lösenorden matchar inte.");
            return form;
        }

        String email = authSession.getEmail();
        if (email == null || email.isEmpty()) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
        UserHandler uh = userDb.getUser(email);
        if (uh == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }

        // CHECK IF VALID PASSWORD
        if (user.getPasswd() == null || user.getPasswd().isEmpty()) {
            form.addError("passwd", "Ange ditt nuvarande lösenord.");
            return form;
        }
        String hashPasswd = PasswordUtil.hash(uh.getSalt() + user.getPasswd());
        if (!(hashPasswd.equals(uh.getPass()))) {
            form.addError("passwd", "Löseordet är fel.");
            return form;
        }

        // EVERYTHING SEEMS TO BE IN ORDER CHANGE USER
        String newSalt = PasswordUtil.randomString(8);
        String newHashPasswd = PasswordUtil.hash(newSalt + user.getNewpasswd());
        uh.setPass(newHashPasswd);
        uh.setSalt(newSalt);
        userDb.storeUser(uh);

        return form;
    }

    @RequestMapping(value = "/user/editemail", method = RequestMethod.POST)
    public @ResponseBody
    FormJSON editEmail(@RequestBody final UserJSON user) {

        FormJSON form = new FormJSON();

        // CHECK SESSION
        if (!authSession.sessionCheck()) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
        String email = authSession.getEmail();

        // EMAIL CHECK
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            form.addError("email", "Ange en e-postadress.");
            return form;
        }
        // CHECK IF VALID EMAIL
        String regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern patternEmail = Pattern.compile(regexEmail);
        if (!(patternEmail.matcher(user.getEmail()).find())) {
            form.addError("email", "Vänligen ange korrekt e-postadress på formatet exempel@exempel.com");
            return form;
        }

        UserHandler uh = userDb.getUser(email);
        // CHECK IF VALID PASSWORD
        if (user.getPasswd() == null || user.getPasswd().isEmpty()) {
            form.addError("passwd", "Ange ditt nuvarande lösenord.");
            return form;
        }
        String hashPasswd = PasswordUtil.hash(uh.getSalt() + user.getPasswd());
        if (!(hashPasswd.equals(uh.getPass()))) {
            form.addError("passwd", "Löseordet är fel.");
            return form;
        }

        // CHECK ACCESSKEY
        if (user.getAccesskey() == null || user.getAccesskey().isEmpty()) {
            // SEND A KEY TO USER'S EMAIL
            String randomKey = PasswordUtil.randomString(5);
            String hashedKey = randomKey; // TODO
            AccessKey newAccessKey = accessKeyManager.create(hashedKey, AccessKeyType.EMAILCHANGE, uh.getUA());
            if (newAccessKey == null) {
                form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
                return form;
            }

            // TODO: SEND EMAIL
            form.addError("accesskey", "En kod har generarats och har skickas till din nuvarande e-postadress.");
            return form;
        }
        // CHECK IF VALID ACCESSKEY
        if (!user.getAccesskey().isEmpty()) {
            AccessKey accessKey = accessKeyService.findByUserAccountAndType(uh.getUA(), AccessKeyType.EMAILCHANGE);
            if (accessKey == null) {
                form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
                return form;
            } else if (!accessKey.getKey().equals(user.getAccesskey())) {
                form.addError("accesskey", "Koden du angav är inkorrekt.");
                return form;
            }
        }

        // EVERYTHING SEEMS TO BE IN ORDER CHANGE USER EMAIL
        // REMOVE KEY FROM DB
        AccessKey accessKey = accessKeyService.findByUserAccountAndType(uh.getUA(), AccessKeyType.EMAILCHANGE);
        accessKeyManager.removeAccessKey(accessKey);
        uh.setEmail(user.getEmail());
        userDb.storeUser(uh);
        authSession.setEmail(user.getEmail());

        return form;
    }

    @RequestMapping(value = "/user/getuserinfo", method = RequestMethod.GET)
    public @ResponseBody
    UserInfoJSON getUserInfo() {

        UserInfoJSON userInfo = new UserInfoJSON();
        // CHECK SESSION
        if (!(authSession.sessionCheck())) {
            return userInfo;
        }
        String email = authSession.getEmail();

        UserHandler ua = userService.getUser(email);

        // SET
        userInfo.setFirstname(ua.getName());
        userInfo.setPhonenumber(ua.getPhoneNumber());
        if(ua.getAddress() != null)
            userInfo.setCompanyname(ua.getAddress().getCompanyName());

        return userInfo;
    }

    @RequestMapping(value = "/user/edituserinfo", method = RequestMethod.PUT)
    public @ResponseBody
    FormJSON editUserInfo(@RequestBody final UserInfoJSON userInfo) {

        System.out.println("* PING user/editUserInfo");
        FormJSON form = new FormJSON();

        if (userInfo == null) {
            form.addError("general", "Ett fel inträffade, vänligen försök igen om en liten stund.");
            return form;
        }

        // CHECK SESSION
        if (!(authSession.sessionCheck())) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();
        UserHandler uh = userService.getUser(email);

        // USER REQUESTED TO CHANGE FIRSNAME
        if (userInfo.getFirstname() != null) {
            uh.setName(userInfo.getFirstname());
            userDb.storeUser(uh);
            return form;
        }

        // USER REQUESTED TO CHANGE LASTNAME
        if (userInfo.getLastname() != null) {
            form.addError("general", "Ej implementerad"); // TODO
            return form;
        }

        // USER REQUESTED TO CHANGE PHONE-NUMBER
        if (userInfo.getPhonenumber() != null) {
            uh.setPhoneNumber(userInfo.getPhonenumber());
            userDb.storeUser(uh);
            return form;
        }

        // USER REQUESTED TO CHANGE COMPANY NAME
        if (userInfo.getCompanyname() != null) {
            if(uh.getAddress() != null) {
                Address address = uh.getAddress();
                address.setCompanyName(userInfo.getCompanyname());
                uh.setAddress(address);
            } else {
                Address new_address = new Address();
                new_address.setCompanyName(userInfo.getCompanyname());
                uh.setAddress(new_address);
            }
            userDb.storeUser(uh);
            return form;
        }

        // YOU SHOULD NOT BE ABLE TO REACH THIS PART
        form.addError("general", "Någon gick fel, vänligen försök igen om en liten stund.");
        return form;
    }

    /**
     * REQUEST KEY FOR PASSWORD RESET
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/passwdreset", method = RequestMethod.POST)
    public @ResponseBody
    FormJSON requestKeyPasswdReset(@RequestBody final UserJSON user) {

        System.out.println("* PING accesskey/forgotpasswd");
        FormJSON form = new FormJSON();

        if (user.getEmail() == null) {
            form.addError("general", "Vänligen ange en e-postadress.");
            return form;
        }
        // CHECK IF VALID EMAIL
        String regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern patternEmail = Pattern.compile(regexEmail);
        if (!(patternEmail.matcher(user.getEmail()).find())) {
            form.addError("general", "Vänligen ange en korrekt e-postadress på formatet exempel@exempel.com");
            return form;
        }

        // CHECK IF EMAIL EXIST
        UserAccount userAccount = userService.getUser(user.getEmail()).getUA();
        if (userAccount == null) {
            form.addError("general", "Det finns inget konto registrerat med den angivna e-postadressen.");
            return form;
        }

        // CHECK IF USER ALREADE REQUESTED A KEY
        AccessKey freshKey = accessKeyService.findByUserAccountAndType(userAccount, AccessKeyType.FORGOTPASSWD);
        if (freshKey != null) {
            Date now = new Date();
            // PREVENT USER GENERATING A NEW KEY
            // IF THE DIFFERENCE BETWEEN NOW-TIME AND PREVIOUS KEYS DATE
            // IS LESSER THAN 20 MIN
            if ((now.getTime() - freshKey.getCreationDate().getTime()) <= 20 * 60 * 1000) {
                form.addError("general", "Du har redan fått en nyckel skickad till din e-postadress.");
                return form;
            }
        }

        // EVERYTHING SEEMS TO BE IN ORDER; CREATE KEY
        String randomKey = PasswordUtil.randomString(15);
        String hashedKey = randomKey; // TODO
        AccessKey newAccessKey = accessKeyManager.create(hashedKey, AccessKeyType.FORGOTPASSWD, userAccount);
        if (newAccessKey == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }

        // EMAIL THE USER
        // TO-DO
        return form;
    }

    /**
     * PASSWORD RESET KEY EXIST
     *
     * @param user
     * @return String
     */
    @RequestMapping(value = "/user/keyexist", method = RequestMethod.POST)
    public @ResponseBody
    UserJSON passwdResetKeyExist(@RequestBody final UserJSON user) {

        UserJSON userJSON = new UserJSON();

        if (user.getAccesskey() == null) {
            return userJSON;
        }
        // CHECK KEY
        AccessKey accessKey = accessKeyService.findByKey(user.getAccesskey());
        if (accessKey == null) {
            return userJSON;
        } else if (accessKey.getType().equals(AccessKeyType.FORGOTPASSWD)) {
            // KEY EXIST
            userJSON.setEmail(accessKey.getUserAccount().getEmail());
            return userJSON;
        }

        return userJSON;
    }

    @RequestMapping(value = "/user/passwdrecovery", method = RequestMethod.POST)
    public @ResponseBody
    FormJSON passwdRecovery(@RequestBody final UserJSON user) {

        FormJSON form = new FormJSON();

        // GET USER
        UserHandler userHandler = userService.getUser(user.getEmail());
        if (userHandler == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }

        // GET KEY
        AccessKey accessKey = accessKeyService.findByUserAccountAndType(userHandler.getUA(), AccessKeyType.FORGOTPASSWD);
        if (accessKey == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        } else if (!accessKey.getKey().equals(user.getAccesskey())) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }

        // PASSWORD CHECK
        if (user.getNewpasswd() == null || user.getNewpasswd().isEmpty()) {
            form.addError("newpasswd", "Vänligen ange ett lösenord.");
            return form;
        }
        if (user.getNewpasswd2() == null || user.getNewpasswd2().isEmpty()) {
            form.addError("newpasswd2", "Vänligen ange ett lösenord.");
            return form;
        }
        if (!(user.getNewpasswd().equals(user.getNewpasswd2()))) {
            form.addError("newpasswd2", "Lösenorden matchar inte.");
            return form;
        }

        // EVERYTHING SEEMS TO BE IN ORDER CHANGE PASSWORD
        String newSalt = PasswordUtil.randomString(8);
        String newHashPasswd = PasswordUtil.hash(newSalt + user.getNewpasswd()); //TO DO
        userHandler.setPass(newHashPasswd);
        userHandler.setSalt(newSalt);
        userDb.storeUser(userHandler);

        // DELETE ACCESS KEY
        accessKeyService.removeByUserAccount(userHandler.getUA());

        return form;
    }

}
