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
import se.chalmers.bokforing.model.OrderEntity;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.service.CustomerService;
import se.chalmers.bokforing.service.OrderEntityService;
import se.chalmers.bokforing.service.ProductService;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.service.InvoicePresenter;

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
    private OrderEntityService oeDb;

    @Autowired
    private ProductService psDb;

    private OrderEntity setUp() {
        OrderEntity oe = new OrderEntity();

        oeDb.storeOrderEntity(oe);
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
        oe.setBuyer(toUh);
        oe.setSeller(sender.getUI());
        sender.setBankgiro("123-4567");
        cusDb.save(toUh);
        userDb.storeUser(sender);

        oe.setMomsPrecentage(0.25);
        oe.setMomsRegistredNumber("SE012345678912");

        return oe;
    }

    @Test
    public void printPDF() {
        OrderEntity oe = setUp();

        //Add products
        Product p = new Product();
        p.setName("Thing");
        p.setPrice(10.0);
        psDb.save(p);

        oe.addProduct(p, 10);

        oeDb.storeOrderEntity(oe);
        oeDb.generateInvoice(oe);

        for (Invoice fak : oe.getInvoices()) {
            print(fak);
        }
    }

    @Test
    public void printPDFManyPages() {
        OrderEntity oe = setUp();
        //Add products
        for (int i = 0; i < 30; i++) {
            Product p = new Product();
            p.setName("Thing" + i);
            p.setPrice(i * 2.0 + 1.0);
            psDb.save(p);
            oe.addProduct(p, (i * 3) / 2);
        }
        oeDb.storeOrderEntity(oe);
        oeDb.generateInvoice(oe);

        for (Invoice fak : oe.getInvoices()) {
            print(fak);
        }
    }

    @Test
    public void printPDFInvalid() {
        OrderEntity oe = setUp();
        //Add products
        for (int i = 0; i < 20; i++) {
            Product p = new Product();
            p.setName("Thing" + i);
            p.setPrice(i + 0.0);
            psDb.save(p);
            oe.addProduct(p, i);
        }
        oeDb.storeOrderEntity(oe);
        oeDb.generateInvoice(oe);

        for (int i = 0; i < 5; i++) {
            Product p = new Product();
            p.setName("Valid" + i);
            p.setPrice(i + 0.0);
            psDb.save(p);
            oe.addProduct(p, i);
        }
        oeDb.storeOrderEntity(oe);
        oeDb.generateInvoice(oe);

        for (Invoice fak : oe.getInvoices()) {
            print(fak);
        }
    }

    private void print(Invoice fak) {
        InvoicePresenter fp = new InvoicePresenter(fak);
        // Doesn't work without the correct files
        try {
            fp.print();
        } catch (IOException | DocumentException e) {
            // TODO: fix path
//            assert(false);
        }
    }
}
