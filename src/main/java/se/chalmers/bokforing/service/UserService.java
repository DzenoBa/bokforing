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
import se.chalmers.bokforing.model.Group;
import se.chalmers.bokforing.persistence.UserRepository;

/**
 *
 * @author victor
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRep;

    public List<UserAccount> getUsersByName(String name) {
        return userRep.findByName(name);
    }
    
    /**
     * Get a user by email
     *
     * @param email of the User
     * @return
     */
    public UserAccount getUser(String email) {
        email = email.toLowerCase();
        return userRep.findByEmail(email);
    }

    /**
     * Gets a user by email and password.
     * Thought to be used as an login attempt.
     *
     * @param email of the User
     * @param pass encrypted password of the user
     * @return
     */
    public UserAccount getUser(String email, String pass) {
        email = email.toLowerCase();
        return userRep.findByEmailAndPass(email, pass);
    }

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
    
    public int updateName (String name, String email){
        return userRep.updateName(name,email);
    }
    
    public int updatePass(String pass, String email){
        return userRep.updatePass(pass, email);
    }

    
    public int updateEmail (String newEmail, String email){
        UserAccount u = userRep.findByEmail(email);
        return userRep.updateEmail(newEmail, u.getId());
    }

    public int updateSalt(String salt, String email){
        return userRep.updateSalt(salt, email);
    }
    
    int updateGroup(Group group, String email){        
return -1;        
//return userRep.updateGroup(group, email);
    }
    
    int updateSessionid(String sessionid, String email){
        return userRep.updateSessionid(sessionid, email);
    }
}
