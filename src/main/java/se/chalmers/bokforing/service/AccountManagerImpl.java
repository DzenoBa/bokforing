/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Account;

/**
 *
 * @author Jakob
 */
@Service
public class AccountManagerImpl implements AccountManager {

    @Autowired
    AccountService service;
    
    @Override
    public Account createAccount(int number, String name) {
        if(!accountIsValid(number, name)) {
            return null;
        }
        
        Account account = new Account();
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
