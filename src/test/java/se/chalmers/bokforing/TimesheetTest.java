/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import java.util.List;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.Product.QuantityType;
import se.chalmers.bokforing.model.Timesheet;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.AccountManager;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.service.CustomerService;
import se.chalmers.bokforing.service.ProductManager;
import se.chalmers.bokforing.service.TimesheetManager;
import se.chalmers.bokforing.service.TimesheetService;
import se.chalmers.bokforing.util.DateUtil;

/**
 *
 * @author Jakob
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class TimesheetTest extends AbstractIntegrationTest {
    
    @Autowired
    private TimesheetManager timesheetManager;
    
    @Autowired
    private TimesheetService timesheetService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ProductManager productManager;
    
    @Autowired
    private AccountManager accountManager;
    
    private UserAccount user;
    private Customer customer;
    
    
    @Before
    public void setup() {
        user = userService.getUser("apa@test.com").getUA();
        
        customer = customerService.findByCustomerNumber(user, 1);
    }
    
    @Test
    @Transactional
    public void createTimesheet() {
        double quantity = 10;
        Account defaultAccount = accountManager.createAccount(2050, "Banankontot");
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "product");
        
        Product product = productManager.createProduct(user, "Bananas", 10.0, QuantityType.KILOGRAM, "Bananas en masse.", defaultAccount);
        
        Timesheet timesheet = timesheetManager.createTimesheet(user, customer, product, quantity, "Köpte lite bananer", DateUtil.getTodaysDate());
        
        // There should only be one in the db
        List<Timesheet> timesheetsFromDb = timesheetService.findAllTimesheets(user, terms).getContent();
        assertEquals(1, timesheetsFromDb.size());
        
        Timesheet timesheetFromDb = timesheetsFromDb.get(0);
        
        assertNotNull(timesheet);
        assertEquals(user, timesheetFromDb.getUserAccount());
        assertEquals(product, timesheetFromDb.getProduct());
        assertEquals(defaultAccount, timesheetFromDb.getProduct().getDefaultAccount());
    }
    
}
