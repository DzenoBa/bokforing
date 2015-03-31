/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.persistence.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
