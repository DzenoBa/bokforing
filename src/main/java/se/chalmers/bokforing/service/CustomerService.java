/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.data.domain.Page;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;

/**
 *
 * @author Jakob
 */
public interface CustomerService {
    
    Page<Customer> findAllCustomers(UserAccount user, PagingAndSortingTerms terms);
    
    Customer findByCustomerNumber(UserAccount user, long customerNumber);
    
    void save(Customer customer);
    
    void delete(Customer customer);
    
}
