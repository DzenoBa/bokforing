/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.AccountType;
import se.chalmers.bokforing.service.AccountManager;
import se.chalmers.bokforing.service.AccountService;

/**
 *
 * @author Jakob
 */
@Service
public class AccountManagerImpl implements AccountManager {

    @Autowired
    private AccountService service;
    
    @Override
    public Account createAccount(int number, String name) {
        if(!accountIsValid(number, name)) {
            return null;
        }
        
        Account account = new Account();

        int firstDigit = Integer.parseInt(Integer.toString(number).substring(0, 1));
        
        // A bit hacky bit should work
        for(AccountType type : AccountType.values()) {
            if(type.getStartingDigit() == firstDigit) {
                account.setAccountType(type);
            }
        } 
        
        account.setNumber(number);
        account.setName(name);
        service.save(account);
        
        return account;
    }

    @Override
    public void removeAccount(Account account) {
        if(account != null) {
            service.delete(null);
        }
    }

    private boolean accountIsValid(int number, String name) {
        // TODO: Validate data, cannot exist already
        return true;
    }
    
}
