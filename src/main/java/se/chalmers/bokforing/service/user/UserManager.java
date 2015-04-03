/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.user;

import se.chalmers.bokforing.model.user.UserHandler;

/**
 *
 * @author Isabelle
 * 
 * Interface for UserManagerImpl
 */
public interface UserManager {
    
     /**
     * Called from the frontend when a new user is created
     * @param userAcc of type UserAccount with email and name
     */
    public void createUser(UserHandler userAcc);
}
