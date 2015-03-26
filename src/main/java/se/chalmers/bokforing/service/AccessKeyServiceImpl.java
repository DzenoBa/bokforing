/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.AccessKey;
import se.chalmers.bokforing.model.AccessKeyType;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.persistence.AccessKeyRepository;

/**
 *
 * @author Dženan
 */
@Service
@Transactional
public class AccessKeyServiceImpl implements AccessKeyService {

    @Autowired
    private AccessKeyRepository repository;
    
    @Override
    public List<AccessKey> findByUserAccount(UserAccount userAccount) {
        return repository.findByUserAccount(userAccount);
    }

    @Override
    public AccessKey findByUserAccountAndType(UserAccount userAccount, AccessKeyType type) {
        return repository.findByUserAccountAndType(userAccount, type);
    }

    @Override
    public void save(AccessKey accessKey) {
        repository.save(accessKey);
    }

    @Override
    public void delete(AccessKey accessKey) {
        repository.delete(accessKey);
    }
    
}
