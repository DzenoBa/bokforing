/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.beans.factory.annotation.Autowired;
import se.chalmers.bokforing.model.UserAccount;

/**
 *
 * @author Isabelle
 * Move code from create in UserController here to match service/manager pattern?
 */
public class UserManagerImpl implements UserManager{
    
    @Autowired 
    private UserService userService;
    
    @Override
    public void createUser(UserAccount userAcc){
        
        //STORE DEAFULT ACCOUNTS AND USER
        
        userService.storeUser(userAcc);
    }
}
