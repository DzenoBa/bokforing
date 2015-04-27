/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import com.lowagie.text.DocumentException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.faktura.Content;
import se.chalmers.bokforing.model.faktura.Faktura;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.persistence.user.UserService;
import se.chalmers.bokforing.service.faktura.FakturaPresenter;

/**
 *
 * @author victor
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class FakturaTest  extends AbstractIntegrationTest {
    @Autowired
    private UserService userDb;
    @Test
    public void printPDF(){
        Faktura fak = new Faktura();
        fak.setFakturaId(0l);
        UserHandler toUh = new UserHandler();
        toUh.setEmail("to@to.com");
        toUh.setName("To Who");
        toUh.setCompanyName("To test is to live.");
        toUh.setCompanyAdr("Road not found 404");
        toUh.setPostCode("333 21");
        UserHandler newUh = new UserHandler();
        newUh.setEmail("from@from.com");
        newUh.setName("From who");
        newUh.setCompanyName("Company from heroes");
        newUh.setCompanyAdr("Highway to Hell");
        newUh.setPostCode("666 42");
        newUh.setPhoneNumber("331-10 10 10");
        newUh.setBankgiro("123-4567");
        fak.setToUser(toUh.getUI());
        fak.setFromUser(newUh.getUI());
        userDb.storeUser(toUh);
        userDb.storeUser(newUh);
        
        Content prods = new Content();
        
        for(int i = 0; i < 15; i++)
            prods.addProduct("Prod " + i, 100.0, i);

        fak.setContent(prods);
        
        fak.setMomsPrecentage(0.25);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fak.getFakturaDate());
        cal.add(Calendar.DAY_OF_MONTH, 15);
        fak.setExpireDate(cal.getTime());
        
        fak.setMomsRegistredNumber("SE012345678912");

        FakturaPresenter fp = new FakturaPresenter(fak);
        try{
            fp.print();
        } catch (IOException | DocumentException e){
            assert(false);
        }
    }
}
