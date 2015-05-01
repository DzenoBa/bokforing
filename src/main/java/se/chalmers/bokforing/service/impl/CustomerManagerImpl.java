/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Address;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.persistence.CustomerRepository;
import se.chalmers.bokforing.service.CustomerManager;
import se.chalmers.bokforing.service.CustomerService;

/**
 *
 * @author Jakob
 */
@Service
public class CustomerManagerImpl implements CustomerManager {

    @Autowired
    private CustomerService service;
    
    @Override
    public Customer createCustomer(UserAccount user, long number, String name, String phoneNumber, Address address) {
        if(customerNumberAlreadyExists(user, number)) {
            return null;
        }
        
        Customer customer = new Customer();
        customer.setCustomerNumber(number);
        customer.setName(name);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);
        customer.setUserAccount(user);
        service.save(customer);
        
        return customer;
    }        

    private boolean customerNumberAlreadyExists(UserAccount user, long number) {
        Customer customer = service.findByCustomerNumber(user, number);
        return customer != null;
    }

    @Override
    public void deleteCustomer(Customer customer) {
        if(customer != null) {
            service.delete(customer);
        }
    }
    
}
