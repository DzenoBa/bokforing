/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.List;
import se.chalmers.bokforing.model.UserGroup;
import se.chalmers.bokforing.model.UserAccount;

/**
 *
 * @author victor
 */
public interface UserService {
    
    public List<UserAccount> getUsersByName(String name);
    
    /**
     * Get a user by email
     *
     * @param email of the User
     * @return UserAccount with the email
     */
    public UserAccount getUser(String email);

    /**
     * Gets a user by email and password.
     * Thought to be used as an login attempt.
     *
     * @param email of the User
     * @param pass encrypted password of the user
     * @return UserAccount with the email and password
     */
    public UserAccount getUser(String email, String pass);
    
    /**
     * Save a new user to the database.
     * @param user 
     */
    public void storeUser(UserAccount user);
    
    /**
     * Update the name of a user
     * @param name
     * @param email
     * @return number of lines changed. Should always be one.
     */
    public int updateName (String name, String email);
    /**
     * Update the password of a user
     * @param pass
     * @param email
     * @return 
     */
    public int updatePass(String pass, String email);

    /**
     * Update the email of a user
     * @param newEmail
     * @param email
     * @return 
     */
    public int updateEmail (String newEmail, String email);

    /**
     * Update the salt of a user
     * @param salt
     * @param email
     * @return 
     */
    public int updateSalt(String salt, String email);
    
    /**
     * Update the group of a user
     * @param group
     * @param email
     * @return 
     */
    public int updateGroup(UserGroup group, String email);
    
    /**
     * Update the sessionid of a user
     * @param sessionid
     * @param email
     * @return 
     */
    public int updateSessionid(String sessionid, String email);
}
