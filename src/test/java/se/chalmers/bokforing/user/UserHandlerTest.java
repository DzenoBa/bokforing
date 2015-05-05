/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.user;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import se.chalmers.bokforing.AbstractIntegrationTest;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.model.UserGroup;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.service.UserManager;
import se.chalmers.bokforing.service.UserService;

/**
 *
 * @author victor
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class UserHandlerTest extends AbstractIntegrationTest {

    @Autowired
    private UserManager manager;

    @Autowired
    private UserService service;

    @Test
    public void testConstructorTests() {
        //A new handler
        UserHandler newUh = new UserHandler();
        assertNotNull(newUh);
        assertNotNull(newUh.getUA());
        assertNotNull(newUh.getUI());

        //store it in db and then load it
        newUh.setEmail("constructor");
        service.storeUser(newUh);
        Long id = newUh.getUA().getId();

        //This one should be the same as newUh as they both share the same UserAccount.
        //Later when one change both will because of that and still be the same.
        UserHandler newUh2 = new UserHandler(newUh.getUA());
        assertTrue(newUh.equals(newUh2));
        newUh2.setUserGroup(UserGroup.Admin);
        assertTrue(newUh.equals(newUh2));

        //The change from before should not have gone through here.
        //So this one should be different
        UserHandler newUh3 = service.getUser("constructor");
        assertNotNull(newUh3);
        assertNotNull(newUh3.getUA());
        assertNotNull(newUh3.getUI());
        assertFalse(newUh.equals(newUh3));
        //So we store the changes and load again
        //NOTICE My modifed variables cause a bit of problem here
        //storing newUh would not update anything as its modified variables are still false.
        //But newUh2 have them set on true so its changes are saved.
        service.storeUser(newUh2);

        //Reload
        newUh3 = service.getUser("constructor");
        assertTrue(newUh.equals(newUh3));
        //The id should not have updated itself
        assertTrue(id.equals(newUh.getUA().getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoEmail() {
        UserHandler noEmail = new UserHandler();
        service.storeUser(noEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyEmail() {
        UserHandler emptyEmail = new UserHandler();
        emptyEmail.setEmail("");
        service.storeUser(emptyEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGroup() {
        UserHandler uh = new UserHandler();
        uh.setEmail("nullTest");
        uh.setUserGroup(null);
        service.storeUser(uh);
    }

    @Test
    public void testIdTest() {
        UserHandler start = new UserHandler();

        start.setEmail("start");
        service.storeUser(start);
        Long id = start.getUA().getId();
        for (int i = 1; i <= 10; i++) {
            UserHandler temp = new UserHandler();
            temp.setEmail("temp" + i);
            service.storeUser(temp);
            assertTrue(id + i == temp.getUA().getId());
        }
    }

    @Test
    public void testChangeToBadEmail() {
        UserHandler goodEmail = new UserHandler();

        goodEmail.setEmail("good");
        boolean passed1 = true, passed2 = true;

        try {
            goodEmail.setEmail("");
            service.storeUser(goodEmail);
        } catch (IllegalArgumentException e) {
            passed1 = true;
        }

        try {
            goodEmail.setEmail(null);
            service.storeUser(goodEmail);
        } catch (IllegalArgumentException e) {
            passed2 = true;
        }

        assertTrue(passed1 && passed2);
    }

    @Test
    public void testGetUserByName() {
        List<UserAccount> usersByName = service.getUsersByName("Jakob");

        // Only one user inserted into database so we should only find one by
        // the name of "Jakob" too
        assertEquals(1, usersByName.size());
    }

    @Test
    public void testGetUser() {
        String email = "hej@hej.com";
        String pass = "jakob";

        UserHandler handler = new UserHandler();
        handler.setEmail(email);
        handler.setPass(pass);

        manager.createUser(handler);

        UserHandler userFromDb = service.getUser(email, pass);
        assertEquals(handler.getEmail(), userFromDb.getEmail());
        assertEquals(handler.getPass(), userFromDb.getPass());

    }
}
