/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import com.lowagie.text.DocumentException;
import java.io.IOException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Address;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Invoice;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.service.CustomerService;
import se.chalmers.bokforing.service.InvoicePresenter;
import se.chalmers.bokforing.service.InvoiceService;
import se.chalmers.bokforing.service.ProductService;
import se.chalmers.bokforing.service.UserService;

/**
 *
 * @author victor
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class InvoiceTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userDb;

    @Autowired
    private CustomerService cusDb;

    @Autowired
    private ProductService psDb;
    
    @Autowired
    private InvoiceService inDb;

    private Invoice setUp() {
        Invoice fak = new Invoice();
        Customer toUh = new Customer();
        toUh.setName("To Who");
        toUh.setCustomerNumber(0l);
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
        fak.setBuyer(toUh);
        fak.setSeller(sender.getUI());
        sender.setBankgiro("123-4567");
        cusDb.save(toUh);
        userDb.storeUser(sender);

        fak.setMoms(0.25);
        fak.setMomsNumber("SE012345678912");
        inDb.storeFaktura(fak);

        return fak;
    }

    @Test
    public void printPDF() {
        Invoice oe = setUp();

        //Add products
        Product p = new Product();
        p.setName("Thing");
        p.setPrice(10.0);
        psDb.save(p);

        oe.addProduct(p, 10);

        inDb.storeFaktura(oe);

        print(oe);
    }

    @Test
    public void printPDFManyPages() {
        Invoice oe = setUp();
        //Add products
        for (int i = 0; i < 30; i++) {
            Product p = new Product();
            p.setName("Thing" + i);
            p.setPrice(i * 2.0 + 1.0);
            psDb.save(p);
            oe.addProduct(p, (i * 3) / 2);
        }
        inDb.storeFaktura(oe);

        String str = print(oe);
        System.out.println(str);
    }

    @Test
    public void printPDFInvalid() {
        Invoice oe = setUp();
        //Add products
        for (int i = 0; i < 20; i++) {
            Product p = new Product();
            p.setName("Thing" + i);
            p.setPrice(i + 0.0);
            psDb.save(p);
            oe.addProduct(p, i);
        }
        inDb.storeFaktura(oe);

        print(oe);
        oe.setValid(false);
        print(oe);
    }

    private String print(Invoice fak) {
        InvoicePresenter fp = new InvoicePresenter(fak);
        String str = null;
        try {
            str = fp.print();
        } catch (IOException | DocumentException e) {
            System.out.println(e.getLocalizedMessage());
            assert(false);
        }
        return str;
    }
}
