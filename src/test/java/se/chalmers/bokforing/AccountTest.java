package se.chalmers.bokforing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.util.InitializationUtil;

/**
 *
 * @author Dženan
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class AccountTest extends AbstractIntegrationTest {

    @Autowired
    private AccountService service;

    @Autowired
    EntityManager em;

    @Autowired
    private InitializationUtil initUtil;

    @Transactional
    @Test
    public void testGetAccountPage() {

        Query query = null;
        for (int i = 0; i < 10; i++) {
            int number = 1000 + i;
            String name = "Konto" + i;

            query = em.createNativeQuery(
                    "INSERT INTO Accounts (number, name) VALUES (?, ?)")
                    .setParameter(1, number)
                    .setParameter(2, name);
            query.executeUpdate();
        }

        // TEST FIND ACCOUNT BY NUMBER
        Account acc1 = service.findAccountByNumber(1005);
        assertNotNull(acc1);
        assertEquals("Konto5", acc1.getName());

        // TEST FIND ALL
        Page<Account> accls = service.findAllAccounts(0, "name", false);
        assertEquals(10, accls.getNumberOfElements());

        List<Account> accls2 = service.findAccountBetween(1002, 1008);
        assertEquals(7, accls2.size());
    }

    @Transactional
    @Test
    public void testInsertDefaultAccounts() {
        initUtil.insertDefaultAccounts();
        
        //MAKE SURE ALL DEFAULT ACCOUNTS EXIST IN DATABASE
        try (BufferedReader br = new BufferedReader(new FileReader("Accounts.txt"))) {
            String line = br.readLine();
            try {
                    while(line != null){
                    int id = Integer.parseInt(line.substring(0, 4));
                    String name = line.substring(4);
                    Account acc = service.findAccountByNumber(id);
                    assertNotNull(acc);
                    assertEquals(name, acc.getName());
                    line = br.readLine();
                    }
            }
            finally{
                
            }
        
         } 
            

         catch (IOException e) {
            //TODO: Catch exception if file with default accounts doesn't exist
        }

    }
}
