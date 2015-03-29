
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.model.AccessKey;
import se.chalmers.bokforing.model.AccessKeyType;
import se.chalmers.bokforing.model.user.UserAccount;

/**
 *
 * @author Dženan
 */
public interface AccessKeyManager {
    
    AccessKey create(String key, AccessKeyType type, UserAccount userAccount);
    
    void removeAccessKey(AccessKey accessKey);
}
