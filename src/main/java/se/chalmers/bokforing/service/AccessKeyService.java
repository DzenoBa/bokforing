
package se.chalmers.bokforing.service;

import java.util.List;
import se.chalmers.bokforing.model.AccessKey;
import se.chalmers.bokforing.model.AccessKeyType;
import se.chalmers.bokforing.model.UserAccount;

/**
 *
 * @author Dženan
 */
public interface AccessKeyService {
    
    List<AccessKey> findByUserAccount(UserAccount userAccount);
    
    AccessKey findByUserAccountAndType(UserAccount userAccount, AccessKeyType type);
    
    AccessKey findByKey(String key);
    
    void save(AccessKey accessKey);
    
    void delete(AccessKey accessKey);
    
    void removeByUserAccount(UserAccount userAccount);
}
