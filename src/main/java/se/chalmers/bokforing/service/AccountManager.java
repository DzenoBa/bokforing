/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.model.Account;

/**
 *
 * @author Jakob
 */
public interface AccountManager {

    Account createAccount(int number, String name);

    void removeAccount(Account account);

}
