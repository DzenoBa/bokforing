/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.Timesheet;
import se.chalmers.bokforing.model.UserAccount;

/**
 *
 * @author Jakob
 */
@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    
    Page<Timesheet> findByUserAccount(UserAccount userAccount, Pageable pageable);
    
}
