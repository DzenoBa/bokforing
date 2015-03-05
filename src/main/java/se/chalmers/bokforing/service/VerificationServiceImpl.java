/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.persistence.VerificationRepository;

/**
 *
 * @author Jakob
 */
@Service
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private VerificationRepository repository;
    
    @Override
    public Page<Verification> findAllVerifications(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Verification findVerificationById(long id) {
        return repository.findById(id);
    }

    @Override
    public void save(Verification verification) {
        repository.save(verification);
    }
    
    @Override
    public Long findHighestId() {
        return repository.findHighestId();
    }
    
}
