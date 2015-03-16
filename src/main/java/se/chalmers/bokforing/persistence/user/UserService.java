/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence.user;

import java.net.URI;
import java.util.Date;
import java.util.List;
import se.chalmers.bokforing.model.user.UserGroup;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.user.UserInfo;

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
    
    public int updateInfo(UserInfo userInfo,String email);
    /**
     * Update the last log in date of a user to the curret moment.
     * @param email
     * @return 
     */
    public int updateLastLogIn(String email);
    
    /**
     * Set the last log in date to date.
     * @param date
     * @param email
     * @return 
     */
    public int updateLastLogIn(Date date, String email);
    
    public int updateName(String name, String email);
    
    public int updatePhoneNumber(String Number, String email);
    
    public int updateCompanyName(String companyName, String email);
    
    public int updateLogo(URI logo, String email);
}
