
package se.chalmers.bokforing;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.AccessKey;
import se.chalmers.bokforing.model.AccessKeyType;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.service.AccessKeyManager;
import se.chalmers.bokforing.service.AccessKeyService;

/**
 *
 * @author Dženan
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class AccessKeyTest extends AbstractIntegrationTest {
    
    @Autowired
    private AccessKeyService service;
    
    @Autowired
    private AccessKeyManager manager;
    
    private UserAccount user;
    
    @Before
    public void setup() {
        user = new UserAccount();
        user.setId(1L);
    }
    
    @Transactional
    @Test
    public void createAccessKey() {
        
        AccessKey newAccessKey =  manager.create("key", AccessKeyType.FORGOTPASSWD, user);
        AccessKey dbAccessKey = service.findByUserAccountAndType(user, AccessKeyType.FORGOTPASSWD);
        
        assertEquals(newAccessKey.getAccesskey(), dbAccessKey.getAccesskey());
    }
    
}
