
package se.chalmers.bokforing.service;

import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.AccessKey;
import se.chalmers.bokforing.model.AccessKeyType;
import se.chalmers.bokforing.model.user.UserAccount;

/**
 *
 * @author Dženan
 */
@Service
public class AccessKeyManagerImpl implements AccessKeyManager {

    @Autowired
    private AccessKeyService service;
    
    @Override
    public AccessKey create(String key, AccessKeyType type, UserAccount userAccount) {
        
        // IT SHOULD ONLY BE ONE KEY PER USER
        service.removeByUserAccount(userAccount);
        
        AccessKey accessKey = new AccessKey();
        accessKey.setKey(key);
        accessKey.setType(type);
        accessKey.setUserAccount(userAccount);
        
        Date todaysDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(todaysDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        accessKey.setCreationDate(cal.getTime());
        
        service.save(accessKey);
        
        return accessKey;
    }

    @Override
    public void removeAccessKey(AccessKey accessKey) {
        
        if(accessKey != null) {
            service.delete(accessKey);
        }
    }
    
}
