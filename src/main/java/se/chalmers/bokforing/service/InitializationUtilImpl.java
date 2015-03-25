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

        File input = new File(getClass().getResource("/accounts.txt").toString().substring(6));
        //System.out.println(input.getAbsolutePath());

    
        //System.out.println(new File(getClass().getResource("accounts.txt").toString()).getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            line = br.readLine();
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
