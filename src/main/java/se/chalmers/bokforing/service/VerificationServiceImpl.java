/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.util.Constants;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.persistence.VerificationRepository;
import se.chalmers.bokforing.persistence.VerificationSpecs;

/**
 *
 * @author Jakob
 */
@Service
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private VerificationRepository repository;
    
    @Override
    public Page<Verification> findAllVerifications(UserAccount user, Pageable pageable) {
        if(user == null) {
            return repository.findAll(pageable);
        } else {
            return repository.findAll(whereUser(user), pageable);
        }
    }

    @Override
    public Verification findVerificationById(UserAccount user, long id) {
        return repository.findByIdAndUserAccount(id, user);
    }

    @Override
    public void save(Verification verification) {
        repository.save(verification);
    }
    
    @Override
    public long findHighestVerificationNumber(UserAccount user) {
        Long res = repository.findHighestId(user.getId());
        return res == null ? 0 : res;
    }

    @Override
    public Page<Verification> findAllVerifications(UserAccount user, Integer pageNumber, String fieldToSortBy, Boolean ascendingSort) {
        PageRequest request = PageRequestParser.getPageRequest(pageNumber, ascendingSort, fieldToSortBy);
        
        return repository.findAll(request);
    }
    
    private Specifications<Verification> whereUser(UserAccount userAccount) {
        return Specifications.where(VerificationSpecs.hasUserAccount(userAccount));
    }

    @Override
    public Verification findByUserAndVerificationNumber(UserAccount user, long verNbr) {
        return repository.findByUserAccountAndVerificationNumber(user, verNbr);
    }
}
