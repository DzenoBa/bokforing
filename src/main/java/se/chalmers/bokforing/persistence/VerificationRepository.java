/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
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
    
}
