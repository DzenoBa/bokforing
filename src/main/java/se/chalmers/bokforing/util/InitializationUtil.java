
package se.chalmers.bokforing.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.management.Query;
import javax.persistence.EntityManager;

/**
 *
 * @author Isabelle
 */
public class InitializationUtil {
        EntityManager em;
    	public static void insertDefaultAccounts() {
		String line;
		// EntityManager em = getEntityManager();
		try (BufferedReader br = new BufferedReader(new FileReader("Accounts.txt"))) {
			line = br.readLine();

			while (line != null) {
				try {
					int id = Integer.parseInt(line.substring(0, 4));
					
					String name = line.substring(4);
					
					line = br.readLine();
                                //       Query query = em.createNativeQuery(
                                //                "INSERT INTO Accounts (id, name) VALUES (" + id + ", '" + name + "')");
                                //        query.executeUpdate();
				}
			
				
				
                                //
				// em.getTransaction().begin();
				// Account account = new Account();
                                // account.setNumber(id);
                                // account.setName(name);
				// em.persist(account);
				//
				// em.getTransaction().commit();
				
				finally {

				}
			}

		} catch (IOException e) {
			line = "nothing";
		}
	}
}
