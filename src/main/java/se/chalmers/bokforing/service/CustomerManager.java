/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.model.Address;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.UserAccount;

/**
 *
 * @author Jakob
 */
public interface CustomerManager {
    
    Customer createCustomer(UserAccount user, long number, String name, String phoneNumber, Address address);
    
    void removeCustomer(Customer customer);
    
}
