/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.model.UserAccount;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.UserGroup;
import se.chalmers.bokforing.persistence.UserRepository;

/**
 *
 * @author victor
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRep;

    @Override
    public List<UserAccount> getUsersByName(String name) {
        return userRep.findByName(name);
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
    public int updateName (String name, String email){
        return userRep.updateName(name,email);
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
}
