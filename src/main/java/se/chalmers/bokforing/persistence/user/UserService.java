/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence.user;

import java.util.List;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.user.UserHandler;

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
    public UserHandler getUser(String email);

    /**
     * Gets a user by email and password.
     * Thought to be used as an login attempt.
     *
     * @param email of the User
     * @param pass encrypted password of the user
     * @return UserAccount with the email and password
     */
    public UserHandler getUser(String email, String pass);
    
    public void storeUser(UserHandler uh);
}
