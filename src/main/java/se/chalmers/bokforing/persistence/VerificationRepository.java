/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Verification;


/**
 *
 * @author Jakob
 */
@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    
    Verification findById(Long id);

    @Query("SELECT MAX(id) FROM Verification")
    Long findHighestId();
    
    Page<Verification> findByCreationDateBetween(Date startDate, Date endDate, Pageable pageable);
    
    Page<Verification> findByOrderByCreationDateAsc(Pageable pageable);
    
    Page<Verification> findByOrderByCreationDateDesc(Pageable pageable);
    
    // it's not this simple since account is in post. maybe we don't want this anyway?
    // Page<Verification> findByAccount(Account account, Pageable pageable);
    
}
