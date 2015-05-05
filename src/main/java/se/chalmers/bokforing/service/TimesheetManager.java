/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.Date;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.Timesheet;
import se.chalmers.bokforing.model.UserAccount;

/**
 *
 * @author Jakob
 */
public interface TimesheetManager {

    Timesheet createTimesheet(UserAccount user, Customer customer, Product product, double quantity, String description, Date date);

    void removeTimesheet(Timesheet timesheet);
}
