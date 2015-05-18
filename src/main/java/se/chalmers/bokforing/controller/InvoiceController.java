
package se.chalmers.bokforing.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.jsonobject.CustomerJSON;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.InvoiceJSON;
import se.chalmers.bokforing.jsonobject.ProductJSON;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Invoice;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.CustomerService;
import se.chalmers.bokforing.service.InvoicePresenter;
import se.chalmers.bokforing.service.InvoiceService;
import se.chalmers.bokforing.service.ProductService;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.session.AuthSession;

/**
 *
 * @author Dženan
 */
@Controller
public class InvoiceController {
    
    @Autowired
    private AuthSession authSession;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ProductService productService;
    
    /*
     * CREATE INVOICE
     */
    @RequestMapping(value = "/invoice/create", method = RequestMethod.POST)
    public @ResponseBody FormJSON create(@RequestBody final InvoiceJSON invoice) {

        FormJSON form = new FormJSON();

        // CHECK SESSION
        if (!authSession.sessionCheck()) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();
        
        // CHECK NAME
        if(invoice.getCustomer() == null) {
            form.addError("customer", "Vänligen ange en kund.");
            return form;
        }
        UserHandler uh = userService.getUser(email);
        Customer c = customerService.findByCustomerNumber(uh.getUA(), invoice.getCustomer().getCustomernumber());
        if(c == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
        
        // PRODUCT CHECK
        if(invoice.getProductls() == null) {
            form.addError("productls", "Vänligen lägg till produkter.");
            return form;
        }
        List<Product> pLs = new ArrayList();
        List<Integer> intLs = new ArrayList();
        for(ProductJSON p : invoice.getProductls()) {
            if(p.getId() == null) {
                form.addError("productls", "Vänligen lägg till produkter.");
                return form;
            }
            if(!(p.getAmount() > 0)) {
                form.addError("productls", "Vänligen ange ett antal för varje produkt.");
                return form;
            }
            Product temp_p = productService.findProductById(uh.getUA(), p.getId());
            if(temp_p == null) {
                form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
                return form;
            }
            pLs.add(temp_p);
            intLs.add(p.getAmount());
        }
        
        // CHECK VAT-NUMBER
        if(invoice.getVatnumber() == null || invoice.getVatnumber().isEmpty()) {
            form.addError("vatnumber", "Vänligen ange ett momsnummer.");
            return form;
        }
        
        // CHECK VAT
        if(!(invoice.getVat() > 0)) {
            form.addError("vat", "Vänligen ange moms.");
            return form;
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER
        Invoice iv = new Invoice();
        iv.setSeller(uh);
        iv.setBuyer(c);
        iv.setFskatt(invoice.getFtax());
        iv.setMomsNumber(invoice.getVatnumber());
        iv.setMoms(invoice.getVat());
        
        for(Product p : pLs) {
            int i = pLs.indexOf(p);
            iv.addProduct(p, intLs.get(i));
        }
        
        invoiceService.storeFaktura(iv);
        return form;
    }
    
    /*
     * GET INVOICES
     */
    @Transactional
    @RequestMapping(value = "/invoice/getinvoices", method = RequestMethod.POST)
    public @ResponseBody List<InvoiceJSON> getInvoices(@RequestBody final InvoiceJSON invoice) {
        
        List<InvoiceJSON> invoiceJSONLs = new ArrayList();
        
        int startPos = 0;
        
        if (!authSession.sessionCheck()) {
            return invoiceJSONLs;
        }
        UserHandler uh = userService.getUser(authSession.getEmail());
        
        if (invoice.getStartrange() > 0) {
            startPos = invoice.getStartrange();
        }
        
        PagingAndSortingTerms terms = new PagingAndSortingTerms(startPos, Boolean.TRUE, "fakturaDate");
        Page<Invoice> invoicePage;
        
        // SEARCH BY USER AND CUSTOMER
        if(invoice.getCustomer() != null && invoice.getCustomer().getCustomernumber() > 0) {
            Customer c = customerService.findByCustomerNumber(uh.getUA(), invoice.getCustomer().getCustomernumber());
            if(c == null)
                return invoiceJSONLs;
            invoicePage = invoiceService.findByCustomer(uh.getUI(), c, terms);
        } else {
            invoicePage = invoiceService.findAllInvoices(uh.getUI(), terms);
        }
        
        for (Invoice iv : invoicePage.getContent()) {
            InvoiceJSON temp = new InvoiceJSON();
            
            CustomerJSON temp_c = new CustomerJSON();
            temp_c.setCustomernumber(iv.getBuyer().getCustomerNumber());
            temp_c.setName(iv.getBuyer().getName());
            temp.setCustomer(temp_c);
            
            List<ProductJSON> temp_pls = new ArrayList();
            for(Product p : iv.getProd()) {
                ProductJSON temp_p = new ProductJSON();
                
                temp_p.setName(p.getName());
                temp_p.setPrice(p.getPrice());
                temp_p.setAmount(iv.getCountList().get(iv.getProd().indexOf(p)));
                
                temp_pls.add(temp_p);
            }
            temp.setProductls(temp_pls);
            
            temp.setFtax(iv.isFtax());
            temp.setVatnumber(iv.getMomsNumber());
            temp.setVat(iv.getMoms());
            temp.setCreationdate(iv.getFakturaDate());
            temp.setExpiredate(iv.getExpireDate());
            
            invoiceJSONLs.add(temp);
        }
        
        return invoiceJSONLs;
    }
    
    /*
     * COUNT INVOICES
     */
    @RequestMapping(value = "/invoice/countinvoices", method = RequestMethod.POST)
    public @ResponseBody long countInvoices(@RequestBody final InvoiceJSON invoice) {

        long size = 0;

        if (!authSession.sessionCheck()) {
            return size;
        }
        UserHandler uh = userService.getUser(authSession.getEmail());

        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.TRUE, "fakturaDate");
        Page<Invoice> invoicePage;
        if(invoice.getCustomer() != null && invoice.getCustomer().getCustomernumber() > 0) {
            Customer c = customerService.findByCustomerNumber(uh.getUA(), invoice.getCustomer().getCustomernumber());
            if(c == null)
                return size;
            invoicePage = invoiceService.findByCustomer(uh.getUI(), c, terms);
        } else {
            invoicePage = invoiceService.findAllInvoices(uh.getUI(), terms);
        }

        size = invoicePage.getTotalElements();
        return size;
    }
    /*InvoicePresenter ip = new InvoicePresenter(oe);
        try {
            ip.print();
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
}
