/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.data.domain.Page;
import se.chalmers.bokforing.model.Timesheet;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;

/**
 *
 * @author Jakob
 */
public interface TimesheetService {

    Timesheet findTimesheet(long id);

    Page<Timesheet> findAllTimesheets(UserAccount user, PagingAndSortingTerms terms);

    void save(Timesheet timesheet);

    void remove(Timesheet timesheet);

}
