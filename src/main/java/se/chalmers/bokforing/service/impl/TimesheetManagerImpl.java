/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.Timesheet;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.service.TimesheetManager;
import se.chalmers.bokforing.service.TimesheetService;

/**
 *
 * @author Jakob
 */
@Service
public class TimesheetManagerImpl implements TimesheetManager {

    @Autowired
    private TimesheetService service;

    @Override
    public Timesheet createTimesheet(UserAccount user, Customer customer, Product product, double quantity, String description, Date date) {
        if (!timeSheetIsValid(user, customer, product, quantity, description, date)) {
            return null;
        }

        Timesheet timesheet = new Timesheet();
        timesheet.setUserAccount(user);
        timesheet.setCustomer(customer);
        timesheet.setProduct(product);
        timesheet.setQuantity(quantity);
        timesheet.setDescription(description);
        timesheet.setDateWorked(date);

        service.save(timesheet);
        return timesheet;
    }

    @Override
    public void removeTimesheet(Timesheet timesheet) {
        service.remove(timesheet);
    }

    private boolean timeSheetIsValid(UserAccount user, Customer customer, Product product, double quantity, String description, Date date) {
        // TODO: validation here
        return true;
    }

}
