package se.chalmers.bokforing.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.AccessKey;
import se.chalmers.bokforing.model.AccessKeyType;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.persistence.AccessKeyRepository;
import se.chalmers.bokforing.service.AccessKeyService;

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
        return repository.findByUserAccountAndKtype(userAccount, type);
    }

    @Override
    public AccessKey findByKey(String key) {
        return repository.findByAkey(key);
    }

    @Override
    public void save(AccessKey accessKey) {
        repository.save(accessKey);
    }

    @Override
    public void delete(AccessKey accessKey) {
        repository.delete(accessKey);
    }

    @Override
    public void removeByUserAccount(UserAccount userAccount) {
        List<AccessKey> ls = findByUserAccount(userAccount);

        if (ls.size() > 0) {
            repository.removeByUserAccount(userAccount);
        }
    }

}
