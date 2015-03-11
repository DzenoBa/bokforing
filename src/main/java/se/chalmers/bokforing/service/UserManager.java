/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.beans.factory.annotation.Autowired;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.model.UserGroup;
import se.chalmers.bokforing.util.InitializationUtil;
import se.chalmers.bokforing.util.PasswordUtil;

/**
 *
 * @author Isabelle
 * Move code from create in UserController here to match service/manager pattern?
 */
public class UserManager {
    
    @Autowired 
    private InitializationUtil initUtil;
    
    public void createUser(){
       
        
        //STORE DEAFULT ACCOUNTS
        initUtil.insertDefaultAccounts();
    }
}
