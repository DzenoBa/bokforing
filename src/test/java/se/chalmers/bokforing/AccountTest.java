package se.chalmers.bokforing;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.service.InitializationUtilImpl;

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
    private InitializationUtilImpl initUtil;

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
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, false, "name");
        Page<Account> accls = service.findAllAccounts(terms);
        assertEquals(10, accls.getNumberOfElements());

        PagingAndSortingTerms terms2 = new PagingAndSortingTerms(0, Boolean.FALSE, "number");
        Page<Account> accls2 = service.findAccountBetween(1002, 1008, terms2);
        assertEquals(7, accls2.getNumberOfElements());
    }

    @Transactional
    @Test
    public void testSearchAccount() {

        int[] numberls = new int[3];
        String[] namels = new String[3];

        numberls[0] = 1230;
        namels[0] = "Krki";
        numberls[1] = 1235;
        namels[1] = "Dzeno";
        numberls[2] = 5230;
        namels[2] = "Dzenan";

        Query query;
        for (int i = 0; i < 3; i++) {
            query = em.createNativeQuery(
                    "INSERT INTO Accounts (number, name) VALUES (?, ?)")
                    .setParameter(1, numberls[i])
                    .setParameter(2, namels[i]);
            query.executeUpdate();
        }

        PagingAndSortingTerms terms1 = new PagingAndSortingTerms(0, Boolean.FALSE, "number");
        Page<Account> accls1 = service.findByNumberLike(123, terms1);
        assertEquals(2, accls1.getNumberOfElements());

        PagingAndSortingTerms terms2 = new PagingAndSortingTerms(0, Boolean.FALSE, "name");
        Page<Account> accls2 = service.findByNameLike("zen", terms2);
        assertEquals(2, accls2.getNumberOfElements());

        accls1 = service.findByNumberLike(1230, terms1);
        assertEquals(1, accls1.getNumberOfElements());

    }

    @Transactional
    @Test
    public void testInsertDefaultAccounts() {

        assertTrue(initUtil.insertDefaultAccounts());
        assertTrue(service.findAllAccounts().size() > 100);
    }

}
