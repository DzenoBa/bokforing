
package se.chalmers.bokforing.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.jsonobject.CustomerJSON;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.ProductJSON;
import se.chalmers.bokforing.jsonobject.TimesheetJSON;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.Timesheet;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.service.CustomerService;
import se.chalmers.bokforing.service.ProductService;
import se.chalmers.bokforing.service.TimesheetManager;
import se.chalmers.bokforing.service.TimesheetService;
import se.chalmers.bokforing.session.AuthSession;

/**
 *
 * @author Dženan
 */
@Controller
public class TimesheetController {
    
    @Autowired 
    private AuthSession authSession;
    
    @Autowired
    private TimesheetService timesheetService;
    
    @Autowired
    private TimesheetManager timesheetManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CustomerService customerService;
    
    /*
     * CREATE TIMESHEET
     */
    @RequestMapping(value = "/timesheet/create", method = RequestMethod.POST)
    public @ResponseBody FormJSON create(@RequestBody final TimesheetJSON timesheet) {
        
        FormJSON form = new FormJSON();
        
        // CHECK SESSION
        if(!authSession.sessionCheck()) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();
        
        // CHECK PRODUCT
        if(timesheet.getProduct() == null || !(timesheet.getProduct().getId() > 0)) {
            form.addError("product", "Vänligen ange en produkt.");
            return form;
        }
        
        // CHECK CUSTOMER
        if(timesheet.getCustomer() == null || !(timesheet.getCustomer().getCustomernumber() > 0)) {
            form.addError("customer", "Vänligen ange en kund.");
            return form;
        } 
        
        // CHECK QUANTITY
        if(timesheet.getQuantity() == null || !(timesheet.getQuantity() > 0)) {
            form.addError("quantity", "Vänligen ange en kvantitet.");
            return form;
        }
        
        String description = "";
        if(timesheet.getDescription() != null && !timesheet.getDescription().isEmpty()) {
            description = timesheet.getDescription();
        }
        
        // CHECK DATE
        if(timesheet.getDate() == null) {
            form.addError("date", "Vänligen ange ett datum.");
            return form;
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER
        UserHandler uh = userService.getUser(email);
        Product product = productService.findProductById(uh.getUA(), timesheet.getProduct().getId());
        Customer customer = customerService.findByCustomerNumber(uh.getUA(), timesheet.getCustomer().getCustomernumber());
        if(product == null || customer == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
        
        // CREATE TIMESHEET
        Timesheet tsDb = timesheetManager.createTimesheet(uh.getUA(), customer, product, 
                timesheet.getQuantity(), description, timesheet.getDate());
        if(tsDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
        }
        
        return form;
    }
    
    /*
     * GET TIMESHEETS
     */
    @RequestMapping(value = "/timesheet/gettimesheets", method = RequestMethod.POST)
    public @ResponseBody List<TimesheetJSON> getTimesheets(@RequestBody final TimesheetJSON timesheet) {
        
        List<TimesheetJSON> timesheetJSONLs = new ArrayList();
        int startPos = 0;
        
        if(!authSession.sessionCheck()) {
            return timesheetJSONLs;
        }
        UserHandler uh = userService.getUser(authSession.getEmail());
        
        if(timesheet.getStartrange() > 0) {
            startPos = timesheet.getStartrange();
        }
        
        PagingAndSortingTerms terms = new PagingAndSortingTerms(startPos, Boolean.TRUE, "dateWorked");
        Page<Timesheet> timesheetPage = timesheetService.findAllTimesheets(uh.getUA(), terms);
        
        for(Timesheet t : timesheetPage.getContent()) {
            TimesheetJSON t_json = new TimesheetJSON();
            ProductJSON p_json = new ProductJSON();
            CustomerJSON c_json = new CustomerJSON();
            
            p_json.setName(t.getProduct().getName());
            p_json.setId(t.getProduct().getId());
            p_json.setPrice(t.getPrice()); // price * quantity
            
            c_json.setName(t.getCustomer().getName());
            c_json.setCustomernumber(t.getCustomer().getCustomerNumber());
            
            t_json.setId(t.getId());
            t_json.setProduct(p_json);
            t_json.setCustomer(c_json);
            t_json.setQuantity(t.getQuantity());
            t_json.setDate(t.getDateWorked());
            t_json.setDescription(t.getDescription());
            
            timesheetJSONLs.add(t_json);
        }
        
        return timesheetJSONLs;
    }
    
    /*
     * COUNT TIMESHEETS
     */
    @RequestMapping(value = "/timesheet/counttimesheets", method = RequestMethod.GET)
    public @ResponseBody long countTimesheets() {
        
        long size = 0;
        
        if(!authSession.sessionCheck()) {
            return size;
        }
        UserHandler uh = userService.getUser(authSession.getEmail());

        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.TRUE, "dateWorked");
        Page<Timesheet> timesheetPage = timesheetService.findAllTimesheets(uh.getUA(), terms);
        
        size = timesheetPage.getTotalElements();
        return size;
    }
    
    /*
     * EDIT TIMESHEET
     */
    @RequestMapping(value = "/timesheet/edit", method = RequestMethod.POST)
    public @ResponseBody FormJSON edit(@RequestBody final TimesheetJSON timesheet) {
        
        FormJSON form = new FormJSON();
        
        // CHECK SESSION
        if(!authSession.sessionCheck()) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();
        
        // CHECK ID
        if(timesheet.getId() == null || !(timesheet.getId() > 0)) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
        
        // CHECK PRODUCT
        if(timesheet.getProduct() == null || !(timesheet.getProduct().getId() > 0)) {
            form.addError("product", "Vänligen ange en produkt.");
            return form;
        }
        
        // CHECK CUSTOMER
        if(timesheet.getCustomer() == null || !(timesheet.getCustomer().getCustomernumber() > 0)) {
            form.addError("customer", "Vänligen ange en kund.");
            return form;
        } 
        
        // CHECK QUANTITY
        if(timesheet.getQuantity() == null || !(timesheet.getQuantity() > 0)) {
            form.addError("quantity", "Vänligen ange en kvantitet.");
            return form;
        }
        
        String description = "";
        if(timesheet.getDescription() != null && !timesheet.getDescription().isEmpty()) {
            description = timesheet.getDescription();
        }
        
        // CHECK DATE
        if(timesheet.getDate() == null) {
            form.addError("date", "Vänligen ange ett datum.");
            return form;
        }
        
        UserHandler uh = userService.getUser(email);
        Product product = productService.findProductById(uh.getUA(), timesheet.getProduct().getId());
        Customer customer = customerService.findByCustomerNumber(uh.getUA(), timesheet.getCustomer().getCustomernumber());
        if(product == null || customer == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER
        Timesheet tsDb = timesheetService.findTimesheet(timesheet.getId());
        if(tsDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
        tsDb.setCustomer(customer);
        tsDb.setProduct(product);
        tsDb.setQuantity(timesheet.getQuantity());
        tsDb.setDescription(description);
        tsDb.setDateWorked(timesheet.getDate());

        timesheetService.save(tsDb);
        
        return form;
    }
    
    /*
     * DELETE TIMESHEET
     */
    @RequestMapping(value = "/timesheet/delete", method = RequestMethod.POST)
    public @ResponseBody FormJSON delete(@RequestBody final TimesheetJSON timesheet) {
        
        FormJSON form = new FormJSON();
        
        // CHECK SESSION
        if(!authSession.sessionCheck()) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();
        
        // CHECK ID
        if(timesheet.getId() == null || !(timesheet.getId() > 0)) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
              
        // EVERYTHING SEEMS TO BE IN ORDER
        UserHandler uh = userService.getUser(email);
        Timesheet tsDb = timesheetService.findTimesheet(timesheet.getId());
        if(tsDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
        
        timesheetService.remove(tsDb);
        
        return form;
    }
}
