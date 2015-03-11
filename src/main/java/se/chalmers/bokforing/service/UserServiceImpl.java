/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.net.URI;
import java.util.Date;
import java.util.LinkedList;
import se.chalmers.bokforing.model.UserAccount;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.UserGroup;
import se.chalmers.bokforing.model.UserInfo;
import se.chalmers.bokforing.persistence.UserInfoRepository;
import se.chalmers.bokforing.persistence.UserRepository;

/**
 *
 * @author victor
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRep;
    
    @Autowired
    private UserInfoRepository infoRep;

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
    public void storeUser(UserAccount user) {
        //TODO Check if the user is vaild
        String email = user.getEmail();
        if (email != null && !email.equals("")) {
            email = email.toLowerCase();
            user.setEmail(email);
            userRep.save(user);
        } else {
            //Invaild User
            //TODO Throw and exception
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
