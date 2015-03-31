package se.chalmers.bokforing.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public boolean insertDefaultAccounts() {
        String line;

        // The substring at the end is to remove the "file:\" part added to the
        // front of the File-object's path. Doesn't work without removing that,
        // path will be invalid.
        // I think we have to do it this way because of maven, it handles
        // resources such as text files in a special way.
        File input = new File(getClass().getResource("/accounts.txt").toString().substring(6));

        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            line = br.readLine().trim();
            while (line != null) {
                int id = Integer.parseInt(line.substring(0, 4));

                String name = line.substring(4);

                Account account = new Account();
                account.setNumber(id);
                account.setName(name);

                accountRep.save(account);

                line = br.readLine();
            }

        } catch (IOException e) {
            //TODO: Catch exception if file with default accounts doesn't exist
            System.out.println(e.toString());
            return false;
        }

        return true;
    }
}
