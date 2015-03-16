/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence.user;

import java.net.URI;
import java.util.Date;
import java.util.LinkedList;
import se.chalmers.bokforing.model.user.UserAccount;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.user.UserGroup;
import se.chalmers.bokforing.model.user.UserInfo;

import se.chalmers.bokforing.service.InitializationUtil;

/**
 *
 * @author victor
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRep;
    
    @Autowired
    private UserInfoRepository infoRep;
    
    @Autowired
    private InitializationUtil initUtil;


    @Override
    public List<UserAccount> getUsersByName(String name) {
        List<UserInfo> uis = infoRep.findByUserName(name);
        List<UserAccount> uas = new LinkedList<>();
        for(UserInfo i: uis){
            uas.add(i.getUa());
        }
        return uas;
    }
    
    @Override
    public UserAccount getUser(String email) {
        email = email.toLowerCase();
        return userRep.findByEmail(email);
    }


    @Override
    public UserAccount getUser(String email, String pass) {
        email = email.toLowerCase();
        return userRep.findByEmailAndPass(email, pass);
    }

    @Override
    /**
     * storeUser.
     * Will store a user to the database.
     * Will throw an exception if the user already exists.
     * @user the user to store.
     */
    public void storeUser(UserAccount user) {
        String email = user.getEmail();
        if (email != null && !email.equals("")) {
            email = email.toLowerCase();
            user.setEmail(email);
            UserInfo ui = new UserInfo();
            
            user.setUserInfo(ui);
            
            infoRep.save(ui);
            userRep.save(user);
            initUtil.insertDefaultAccounts();
        } else {
            //A very simple errorcheck.
            throw new IllegalArgumentException("\"" + email + "\"" + "is not a vaild email.");
        }
    }
    
    @Override
    public int updatePass(String pass, String email){
        return userRep.updatePass(pass, email);
    }

    @Override
    public int updateEmail (String newEmail, String email){
        UserAccount u = userRep.findByEmail(email);
        return userRep.updateEmail(newEmail, u.getId());
    }

    @Override
    public int updateSalt(String salt, String email){
        return userRep.updateSalt(salt, email);
    }
    
    @Override
    public int updateGroup(UserGroup group, String email){        
        return userRep.updateGroup(group, email);
    }
    
    @Override
    public int updateSessionid(String sessionid, String email){
        return userRep.updateSessionid(sessionid, email);
    }
    
    @Override
    public int updateInfo(UserInfo userInfo, String email){
        return userRep.updateUserInfo(userInfo, email);
    }

    @Override
    public int updateLastLogIn(String email) {
        return updateLastLogIn(new Date(), email);
    }

    @Override
    public int updateLastLogIn(Date date, String email) {
        UserInfo ui = userRep.findByEmail(email).getUseInfo();
        return infoRep.updateLastLogIn(date, ui.getUserInfoId());
    }

    @Override
    public int updateName(final String name, final String email) {
        UserInfo ui = userRep.findByEmail(email).getUseInfo();
        return infoRep.updateName(name, ui.getUserInfoId());
    }

    @Override
    public int updatePhoneNumber(String Number, final String email) {
        UserInfo ui = userRep.findByEmail(email).getUseInfo();
        return infoRep.updatePhoneNumber(Number, ui.getUserInfoId());
    }

    @Override
    public int updateCompanyName(String companyName, String email) {
        UserInfo ui = userRep.findByEmail(email).getUseInfo();
        return infoRep.updateCompanyName(companyName, ui.getUserInfoId());
    }

    @Override
    public int updateLogo(URI logo,String email) {
        UserInfo ui = userRep.findByEmail(email).getUseInfo();
        return infoRep.updateLogo(logo, ui.getUserInfoId());
    }
}
