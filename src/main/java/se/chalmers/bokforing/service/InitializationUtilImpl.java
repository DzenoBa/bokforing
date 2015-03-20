package se.chalmers.bokforing.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.persistence.AccountRepository;

/**
 *
 * @author Isabelle
 */
@Service
public class InitializationUtilImpl implements InitializationUtil {

    @Autowired
    private AccountRepository accountRep;

    @Override
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


                } finally {
            //TODO: Catch exception if first four chars aren't numbers 
                }
            }

        } catch (IOException e) {
            //TODO: Catch exception if file with default accounts doesn't exist
        }
    }
}
