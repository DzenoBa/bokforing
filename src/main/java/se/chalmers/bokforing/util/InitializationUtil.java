package se.chalmers.bokforing.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.persistence.AccountRepository;

/**
 *
 * @author Isabelle
 */
public class InitializationUtil {

//    @Autowired
//    public EntityManager em;
    @Autowired
    private AccountRepository accountRep;

    public void insertDefaultAccounts() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("Accounts.txt"))) {
            line = br.readLine();

            while (line != null) {
                try {
                    int id = Integer.parseInt(line.substring(0, 4));

                    String name = line.substring(4);

                    Account account = new Account();
                    account.setNumber(id);
                    account.setName(name);
                    accountRep.save(account);
                    line = br.readLine();

//                    Query query = em.createNativeQuery(
//                            "INSERT INTO Accounts (id, name) VALUES (" + id + ", '" + name + "')");
//                    query.executeUpdate();
//                    em.getTransaction().begin();
//                    em.persist(account);
//                    em.getTransaction().commit();
                } finally {
            //TODO: Catch exception if first four chars aren't numbers 
                }
            }

        } catch (IOException e) {
            //TODO: Catch exception if file with default accounts doesn't exist
        }
    }
}
