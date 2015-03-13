/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.CustomerManager;
import se.chalmers.bokforing.service.CustomerService;

/**
 *
 * @author Jakob
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class CustomerTest extends AbstractIntegrationTest {
    
    static final int INSERTED_BEFORE_TEST = 1;

    @Autowired
    CustomerManager manager;
    
    @Autowired
    CustomerService service;
    
    @Autowired
    EntityManager em;
    
    private UserAccount user;
    
    @Before
    public void setup() {
        user = new UserAccount();
        user.setId(1L);
    }
    
    @Transactional
    @Test
    public void testCreateCustomer() {
        long number = 999;
        
        Customer customer = manager.createCustomer(user, number, null, null, null);
        Customer customerFromDb = service.findByCustomerNumber(user, number);
        
        assertEquals(customer.getCustomerNumber(), customerFromDb.getCustomerNumber());
    }
    
    @Transactional
    @Test
    public void testGetCustomerPage() {
        Query query = null;
        
        int inserted = 10;
        
        for(int i = 0; i < inserted; i++) {
            int number = 10 + i;
            String name = "Jakob"+i;
            
            query = em.createNativeQuery(
                    "INSERT INTO Customers (userAccount_id, customerNumber, name) VALUES (1, ?, ?)")
                    .setParameter(1, number)
                    .setParameter(2, name);
            query.executeUpdate();
        }
        
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "customerNumber");
        Page<Customer> customers = service.findAllCustomers(user, terms);
        
        assertEquals(inserted + INSERTED_BEFORE_TEST, customers.getTotalElements());
    }
    
}
