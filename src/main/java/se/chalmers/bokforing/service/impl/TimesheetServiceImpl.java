/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import java.util.List;
import se.chalmers.bokforing.persistence.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.Timesheet;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.TimesheetService;

/**
 *
 * @author Jakob
 */
@Transactional
@Service
public class TimesheetServiceImpl implements TimesheetService {

    @Autowired
    private TimesheetRepository repository;
    
    @Override
    public Timesheet findTimesheet(long id) {
        return repository.findOne(id);
    }

    @Override
    public Page<Timesheet> findAllTimesheets(UserAccount user, PagingAndSortingTerms terms) {
        if(user == null) {
            return null;
        }
        
        PageRequest pageRequest = terms.getPageRequest();
        
        return repository.findByUserAccount(user, pageRequest);
    }
    
    @Override
    public void save(Timesheet timesheet) {
        if(timesheet != null) {
            repository.save(timesheet);
        }
    }

    @Override
    public void remove(Timesheet timesheet) {
        if(timesheet != null) {
            repository.delete(timesheet);
        }
    }

    
}
