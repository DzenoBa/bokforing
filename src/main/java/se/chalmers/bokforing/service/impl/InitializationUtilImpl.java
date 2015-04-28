package se.chalmers.bokforing.service.impl;

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
import se.chalmers.bokforing.service.AccountManager;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.service.InitializationUtil;

/**
 *
 * @author Isabelle
 */
@Service
public class InitializationUtilImpl implements InitializationUtil {

    @Autowired
    private AccountService service;
    
    @Autowired
    private AccountManager accMan;

    @Override
    public boolean insertDefaultAccounts() {

        // The substring at the end is to remove the "file:\" part added to the
        // front of the File-object's path. Doesn't work without removing that,
        // path will be invalid.
        // I think we have to do it this way because of maven, it handles
        // resources such as text files in a special way.
        File input = new File(getClass().getResource("/accounts.txt").toString().substring(6));

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(input));
            helper(br);

        }catch (IOException e){
            input = new File("/" + getClass().getResource("/accounts.txt").toString().substring(6));
            try{
            br = new BufferedReader(new FileReader(input));
            helper(br);
            }catch (IOException e1){
                return false;
            }
        }

        return true;
    }
    
    
    private void helper(BufferedReader br) throws NumberFormatException, IOException {
        String line = br.readLine().trim();
        while (line != null) {
            int id = Integer.parseInt(line.substring(0, 4));
            String name = line.substring(4);
            accMan.createAccount(id, name);
            line = br.readLine();
        }
    }

}
