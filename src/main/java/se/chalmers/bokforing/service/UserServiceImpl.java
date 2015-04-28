/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.LinkedList;
import se.chalmers.bokforing.model.user.UserAccount;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.model.user.UserInfo;
import se.chalmers.bokforing.persistence.user.UserInfoRepository;
import se.chalmers.bokforing.persistence.user.UserRepository;

/**
 * Implimentation of userService.
 * This service acts as a proxy to the userAccount and userInfo repository.
 * In a way when function calls are done in java there are sent here to be translateded into sql querrys.
 * @author victor
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRep;
    
    @Autowired
    private UserInfoRepository infoRep;

    @Override
    public List<UserAccount> getUsersByName(String name) {
        List<UserInfo> uis = infoRep.findByUserName(name);
        List<UserAccount> uas = new LinkedList<>();
        for(UserInfo i: uis){
            uas.add(i.getUa());
        }
        return uas;
    }
    
    @Override
    public UserHandler getUser(String email) {
        email = email.toLowerCase();
        UserAccount ua = userRep.findByEmail(email);
        if(ua != null)
            return new UserHandler(ua);
        else 
            return null;
    }

    @Override
    public UserHandler getUser(String email, String pass) {
        email = email.toLowerCase();
        UserAccount ua = userRep.findByEmailAndPass(email, pass);
        if(ua != null)
            return new UserHandler(ua);
        else
            return null;
    }

    @Override
    /**
     * storeUser.
     * Will store a user to the database.
     * @user the user to store.
     */
    public void storeUser(UserHandler uh) {
        if(uh.getUA().getEmail() == null || uh.getUA().getEmail().equals("")){
            throw new IllegalArgumentException("That is not a valid email");
        }
        if(uh.getModifedUser())
            userRep.save(uh.getUA());
        if(uh.getModifedInfo())
            infoRep.save(uh.getUI());
        //initUtil.insertDefaultAccounts();
        uh.resetMod();
    }
}
