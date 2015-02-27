/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.model.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.persistence.UserRepository;

/**
 *
 * @author victor
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRep;

    public List<User> getUsersByName(String name) {
        return userRep.findByName(name);
    }
    
    /**
     * Get a user by email
     *
     * @param email of the User
     * @return
     */
    public User getUser(String email) {
        email = email.toLowerCase();
        return userRep.findByEmail(email);
    }

    /**
     * Gets a user by email and password.
     * Thought to be used as an login attempt.
     *
     * @param email of the User
     * @param pass Unencrypted password of the user
     * @return
     */
    public User getUser(String email, String pass) {
        email = email.toLowerCase();
        pass = encryptString(pass);
        return userRep.findByEmailAndPass(email, pass);
    }

    public void storeUser(User user) {
        //TODO Check if the user is vaild
        String email = user.getEmail();
        String pass = user.getPass();
        if (email != null && !email.equals("")
                && pass != null && !pass.equals("")) {
            email = email.toLowerCase();
            user.setEmail(email);
            pass = encryptString(pass);
            user.setPass(pass);
            userRep.save(user);
        } else {
            //Invaild User
            //TODO Throw and exception
        }
    }

    private String encryptString(String stringToEncrypt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(stringToEncrypt.getBytes());
            String encryptedString = new String(messageDigest.digest());
            return encryptedString;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return stringToEncrypt;
        }
    }
}
