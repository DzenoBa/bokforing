/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.user.UserHandler;

/**
 *
 * @author Isabelle
 * Move code from create in UserController here to match service/manager pattern?
 */
@Service
public class UserManagerImpl implements UserManager{
    
    @Autowired 
    private UserService userService;
    
    @Override
    public void createUser(UserHandler userAcc){
        
        //STORE DEAFULT ACCOUNTS AND USER
        
        userService.storeUser(userAcc);
    }
    
    @Override
    public void addFavoriteAccount(UserHandler userHandler, Account account) {
        UserAccount ua = userHandler.getUA();
        Set<Account> favoriteAccounts = ua.getFavoriteAccounts();
        
        if(favoriteAccounts == null) {
            favoriteAccounts = new HashSet<>();
        }
        
        favoriteAccounts.add(account);
        
        userService.storeUser(userHandler);
    }
    
    @Override
    public boolean removeFavoriteAccount(UserHandler userHandler, Account account) {
        UserAccount ua = userHandler.getUA();
        Set<Account> favoriteAccounts = ua.getFavoriteAccounts();
        
        if(favoriteAccounts == null) {
            return false;
        }
        
        boolean success = favoriteAccounts.remove(account);
        if(success) {
            userService.storeUser(userHandler);
        }
        
        return success;
    }
}
