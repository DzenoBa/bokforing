/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.persistence.CustomerRepository;

/**
 *
 * @author Jakob
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;
    
    @Override
    public Page<Customer> findAllCustomers(UserAccount user, PagingAndSortingTerms terms) {
        PageRequest request = terms.getPageRequest();
     
        return repository.findByUserAccount(user, request);
    }
    
    @Override
    public Customer findByCustomerNumber(UserAccount user, long customerNumber) {
        return repository.findByUserAccountAndCustomerNumber(user, customerNumber);
    }

    @Override
    public void save(Customer customer) {
        repository.save(customer);
    }
    
    @Override
    public void delete(Customer customer) {
        repository.delete(customer);
    }

    @Override
    public Page<Customer> findByNameLike(UserAccount user, String name, PagingAndSortingTerms terms) {
        if(user == null || name == null || name.isEmpty() || terms == null) {
            return null;
        }
        
        PageRequest request = terms.getPageRequest();
        
        return repository.findByUserAccountAndNameContainingIgnoreCase(user, name, request);
    }
}
