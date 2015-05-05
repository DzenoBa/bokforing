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
import se.chalmers.bokforing.model.Address;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.service.CustomerManager;
import se.chalmers.bokforing.service.CustomerService;
import se.chalmers.bokforing.session.AuthSession;

/**
 *
 * @author Dženan
 */
@Controller
public class CustomerController {

    @Autowired
    private AuthSession authSession;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerManager customerManager;

    @Autowired
    private UserService userService;

    /*
     * CREATE PRODUCT
     */
    @RequestMapping(value = "/customer/create", method = RequestMethod.POST)
    public @ResponseBody
    FormJSON create(@RequestBody final CustomerJSON customer) {

        FormJSON form = new FormJSON();

        // CHECK SESSION
        if (!authSession.sessionCheck()) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();

        // CHECK CUSTOMER NUMBER
        if (customer.getCustomernumber() == null || !(customer.getCustomernumber() > 0)) {
            form.addError("customernumber", "Vänligen ange ett kund nummer");
            return form;
        }

        UserHandler uh = userService.getUser(email);
        // CHECK IF CUSTOMER NUMBER EXIST
        Customer custCheck = customerService.findByCustomerNumber(uh.getUA(), customer.getCustomernumber());
        if (custCheck != null) {
            form.addError("customernumber", "Kundnummret är redan registrerat.");
            return form;
        }

        // CHECK NAME
        if (customer.getName() == null || customer.getName().isEmpty()) {
            form.addError("name", "Vänligen ange ett namn.");
            return form;
        }

        // CHECK PHONE NUMBER
        if (customer.getPhonenumber() == null || customer.getPhonenumber().isEmpty()) {
            form.addError("phonenumber", "Vänligen ange ett telefonnummer.");
            return form;
        }

        // CHECK STREET
        if (customer.getStreet() == null || customer.getStreet().isEmpty()) {
            form.addError("street", "Vänligen ange gatuadress.");
            return form;
        }

        // CHECK POSTAL CODE
        if (customer.getPostalcode() == null || customer.getPostalcode().isEmpty()) {
            form.addError("city", "Vänligen ange ett postnummer.");
            return form;
        }

        // CHECK CITY
        if (customer.getCity() == null || customer.getCity().isEmpty()) {
            form.addError("city", "Vänligen ange en stad.");
            return form;
        }

        // CHECK COUNTRY
        if (customer.getCountry() == null || customer.getCountry().isEmpty()) {
            form.addError("country", "Vänligen ange ett land.");
            return form;
        }

        // EVERYTHING SEEMS TO BE IN ORDER
        Address address = new Address();
        address.setStreetNameAndNumber(customer.getStreet());
        address.setPostalCode(customer.getPostalcode());
        address.setPostTown(customer.getCity());
        address.setCountry(customer.getCountry());

        Customer cDb = customerManager.createCustomer(uh.getUA(), customer.getCustomernumber(),
                customer.getName(), customer.getPhonenumber(), address);
        if (cDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
        }
        return form;
    }

    /*
     * GET CUSTOMERS
     */
    @RequestMapping(value = "/customer/getcustomers", method = RequestMethod.POST)
    public @ResponseBody
    List<CustomerJSON> getCustomers(@RequestBody final CustomerJSON customer) {

        List<CustomerJSON> customerJSONLs = new ArrayList();
        int startPos = 0;

        if (!authSession.sessionCheck()) {
            return customerJSONLs;
        }
        UserHandler ua = userService.getUser(authSession.getEmail());

        if (customer.getStartrange() > 0) {
            startPos = customer.getStartrange();
        }

        PagingAndSortingTerms terms = new PagingAndSortingTerms(startPos, Boolean.TRUE, "name");
        Page<Customer> customerPage;
        if (customer.getName() != null && !customer.getName().isEmpty()) {
            customerPage = customerService.findByNameLike(ua.getUA(), customer.getName(), terms);
        } else {
            customerPage = customerService.findAllCustomers(ua.getUA(), terms);
        }

        for (Customer c : customerPage.getContent()) {
            CustomerJSON temp = new CustomerJSON();
            temp.setCustomernumber(c.getCustomerNumber());
            temp.setName(c.getName());
            temp.setPhonenumber(c.getPhoneNumber());
            temp.setStreet(c.getAddress().getStreetNameAndNumber());
            temp.setPostalcode(c.getAddress().getPostalCode());
            temp.setCity(c.getAddress().getPostTown());
            temp.setCountry(c.getAddress().getCountry());

            customerJSONLs.add(temp);
        }

        return customerJSONLs;
    }

    /*
     * COUNT CUSTOMERS
     */
    @RequestMapping(value = "/customer/countcustomers", method = RequestMethod.POST)
    public @ResponseBody
    long countCustomers(@RequestBody final CustomerJSON customer) {

        long size = 0;

        if (!authSession.sessionCheck()) {
            return size;
        }
        UserHandler ua = userService.getUser(authSession.getEmail());

        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.TRUE, "name");
        Page<Customer> customerPage;
        if (customer.getName() != null && !customer.getName().isEmpty()) {
            customerPage = customerService.findByNameLike(ua.getUA(), customer.getName(), terms);
        } else {
            customerPage = customerService.findAllCustomers(ua.getUA(), terms);
        }

        size = customerPage.getTotalElements();
        return size;
    }

    /*
     * EDIT CUSTOMER
     */
    @RequestMapping(value = "/customer/edit", method = RequestMethod.POST)
    public @ResponseBody
    FormJSON edit(@RequestBody final CustomerJSON customer) {

        FormJSON form = new FormJSON();

        // CHECK SESSION
        if (!authSession.sessionCheck()) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();

        // CHECK CUSTOMER NUMBER
        if (customer.getCustomernumber() == null || !(customer.getCustomernumber() > 0)) {
            form.addError("customernumber", "Vänligen ange ett kundnummer.");
            return form;
        }

        // CHECK NAME
        if (customer.getName() == null || customer.getName().isEmpty()) {
            form.addError("name", "Vänligen ange ett namn.");
            return form;
        }

        // CHECK PHONE NUMBER
        if (customer.getPhonenumber() == null || customer.getPhonenumber().isEmpty()) {
            form.addError("phonenumber", "Vänligen ange ett telefonnummer.");
            return form;
        }

        // CHECK STREET
        if (customer.getStreet() == null || customer.getStreet().isEmpty()) {
            form.addError("street", "Vänligen ange gatuadress.");
            return form;
        }

        // CHECK POSTAL CODE
        if (customer.getPostalcode() == null || customer.getPostalcode().isEmpty()) {
            form.addError("city", "Vänligen ange ett postnummer.");
            return form;
        }

        // CHECK CITY
        if (customer.getCity() == null || customer.getCity().isEmpty()) {
            form.addError("city", "Vänligen ange en stad.");
            return form;
        }

        // CHECK COUNTRY
        if (customer.getCountry() == null || customer.getCountry().isEmpty()) {
            form.addError("country", "Vänligen ange ett land.");
            return form;
        }

        // EVERYTHING SEEMS TO BE IN ORDER
        UserHandler uh = userService.getUser(email);
        Customer cDb = customerService.findByCustomerNumber(uh.getUA(), customer.getCustomernumber());
        if (cDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
        }
        cDb.setName(customer.getName());
        cDb.setPhoneNumber(customer.getPhonenumber());

        Address address = cDb.getAddress();
        address.setStreetNameAndNumber(customer.getStreet());
        address.setPostalCode(customer.getPostalcode());
        address.setPostTown(customer.getCity());
        address.setCountry(customer.getCountry());
        cDb.setAddress(address);

        customerService.save(cDb);

        return form;
    }

    /*
     * DELETE CUSTOMER
     */
    @RequestMapping(value = "/customer/delete", method = RequestMethod.POST)
    public @ResponseBody
    FormJSON delete(@RequestBody final CustomerJSON customer) {

        FormJSON form = new FormJSON();

        // CHECK SESSION
        if (!authSession.sessionCheck()) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();

        // CHECK CUSTOMER NUMBER
        if (customer.getCustomernumber() == null || !(customer.getCustomernumber() > 0)) {
            form.addError("genaral", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }

        // EVERYTHING SEEMS TO BE IN ORDER
        UserHandler uh = userService.getUser(email);
        Customer cDb = customerService.findByCustomerNumber(uh.getUA(), customer.getCustomernumber());
        if (cDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }

        customerManager.deleteCustomer(cDb);

        return form;
    }
}
