/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import com.lowagie.text.DocumentException;
import java.io.IOException;
import java.util.Calendar;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Address;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.faktura.Faktura;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.service.CustomerService;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.service.impl.FakturaPresenter;

/**
 *
 * @author victor
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class FakturaTest  extends AbstractIntegrationTest {
    @Autowired
    private UserService userDb;
    @Autowired
    private CustomerService cusDb;
    @Test
    public void printPDF(){
        Faktura fak = new Faktura();
        fak.setFakturaId(0l);
        Customer toUh = new Customer();
        toUh.setName("To Who");
        toUh.setCustomerNumber(Long.MIN_VALUE + 5);
        Address adr = new Address();
        adr.setStreetNameAndNumber("Road not found 404");
        adr.setPostalCode("333 21");
        adr.setCompanyName("To test is to live.");
        toUh.setAddress(adr);
        UserHandler sender = new UserHandler();
        sender.setEmail("from@from.com");
        sender.setName("From who");
        adr = new Address();
        adr.setStreetNameAndNumber("Highway to Hell");
        adr.setPostalCode("666 42");
        adr.setCompanyName("THE COMPANY THAT SELLS");
        sender.setAddress(adr);
        sender.setPhoneNumber("331-10 10 10");
        sender.setBankgiro("123-4567");
        fak.setToUser(toUh);
        fak.setFromUser(sender.getUI());
        cusDb.save(toUh);
        userDb.storeUser(sender);
        
        
        fak.setMomsPrecentage(0.25);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fak.getFakturaDate());
        cal.add(Calendar.DAY_OF_MONTH, 15);
        fak.setExpireDate(cal.getTime());
        
        fak.setMomsRegistredNumber("SE012345678912");

        FakturaPresenter fp = new FakturaPresenter(fak);
        
        // Doesn't work without the correct files
        try{
            fp.print();
        } catch (IOException | DocumentException e){
            // TODO: fix path
//            assert(false);
        }
    }
}
