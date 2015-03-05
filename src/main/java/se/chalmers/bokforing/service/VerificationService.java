/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Verification;

/**
 * Service for querying the database for verification entities.
 * Note that this interface must not provide any delete or update methods,
 * as this would violate Swedish accounting laws.
 * 
 * @author Jakob
 */
public interface VerificationService {
    
    Page<Verification> findAllVerifications(Pageable pageable);
    
    Verification findVerificationById(long id);
    
    Long findHighestId();
    
    void save(Verification verification);
}
