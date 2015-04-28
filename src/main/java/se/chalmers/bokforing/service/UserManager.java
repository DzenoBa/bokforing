/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.user.UserAccount;
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
    
    void addFavoriteAccount(UserHandler user, Account account);
    
    boolean removeFavoriteAccount(UserHandler user, Account account);
}
