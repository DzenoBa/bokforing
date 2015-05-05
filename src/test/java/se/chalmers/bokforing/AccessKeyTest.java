package se.chalmers.bokforing;

import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.AccessKey;
import se.chalmers.bokforing.model.AccessKeyType;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.service.AccessKeyManager;
import se.chalmers.bokforing.service.AccessKeyService;
import se.chalmers.bokforing.service.UserService;

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

    @Autowired
    UserService userService;

    private UserAccount user;

    @Before
    public void setup() {
        user = userService.getUser("apa@test.com").getUA();
    }

    @Transactional
    @Test
    public void createAccessKey() {

        AccessKey newAccessKey = manager.create("key", AccessKeyType.FORGOTPASSWD, user);
        AccessKey dbAccessKey = service.findByUserAccountAndType(user, AccessKeyType.FORGOTPASSWD);

        assertEquals(newAccessKey.getKey(), dbAccessKey.getKey());
    }

    @Transactional
    @Test
    public void removeAccessKey() {

        for (int i = 0; i < 10; i++) {
            String key = "Key" + i;

            manager.create(key, AccessKeyType.FORGOTPASSWD, user);
        }

        // ONLY ONE SHOULD BE FOUND
        List<AccessKey> ls = service.findByUserAccount(user);
        assertEquals(1, ls.size());

        service.removeByUserAccount(user);
        ls = service.findByUserAccount(user);
        assertEquals(0, ls.size());
    }

}
