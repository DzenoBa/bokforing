/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author victor
 */
public class UserDb {

    public static List<UserEnt> getUsersByName(UserRepository userRep,String name) {
        return userRep.findByName(name);
    }
    
    /**
     * Get a user by email
     *
     * @param userRep
     * @param email of the User
     * @return
     */
    public static UserEnt getUser(UserRepository userRep,String email) {
        return userRep.findByEmail(email);
    }

    /**
     * Gets a user by email and password
     *
     * @param userRep
     * @param email of the User
     * @param pass Unencrypted password of the user
     * @return
     */
    public static UserEnt getUser(UserRepository userRep,String email, String pass) {
        pass = encryptString(pass);
        return userRep.findByEmailAndPass(email, pass);
    }

    public static void storeUser(UserRepository userRep,UserEnt user) {
        //TODO Check if the user is vaild
        String pass = user.getPass();
        if (user.getEmail() != null && !user.getEmail().equals("")
                && pass != null && !pass.equals("")) {
            pass = encryptString(pass);
            user.setPass(pass);
            userRep.save(user);
        } else {
            //Invaild User
            //TODO Throw and exception
        }
    }

    private static String encryptString(String stringToEncrypt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(stringToEncrypt.getBytes());
            String encryptedString = new String(messageDigest.digest());
            return encryptedString;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserDb.class.getName()).log(Level.SEVERE, null, ex);
            return stringToEncrypt;
        }
    }
}
