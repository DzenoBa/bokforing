
package se.chalmers.bokforing.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.jsonobject.CustomerJSON;
import se.chalmers.bokforing.jsonobject.InvoiceJSON;
import se.chalmers.bokforing.jsonobject.ProductJSON;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Invoice;
import se.chalmers.bokforing.model.OrderEntity;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.CustomerService;
import se.chalmers.bokforing.service.InvoiceService;
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
    private InvoiceService invoiceService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomerService customerService;
    
    /*
     * GET INVOICES
     */
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
            invoicePage = invoiceService.findByCustomer(uh.getUA(), c, terms);
        } else {
            invoicePage = invoiceService.findByUser(uh.getUA(), terms);
        }
        
        for (Invoice i : invoicePage.getContent()) {
            InvoiceJSON temp = new InvoiceJSON();
            
            OrderEntity oe = i.getOrderEntity();
            
            CustomerJSON temp_c = new CustomerJSON();
            temp_c.setCustomernumber(oe.getBuyer().getCustomerNumber());
            temp_c.setName(oe.getBuyer().getName());
            temp.setCustomer(temp_c);
            
            List<ProductJSON> temp_pls = new ArrayList();
            for(Product p : oe.getProd()) {
                ProductJSON temp_p = new ProductJSON();
                
                temp_p.setName(p.getName());
                temp_p.setPrice(p.getPrice());
                temp_p.setAmount(oe.getCountList().get(oe.getProd().indexOf(p)));
                
                temp_pls.add(temp_p);
            }
            temp.setProductls(temp_pls);
            
            temp.setFtax(oe.isFskatt());
            temp.setVatnumber(oe.getMomsRegistredNumber());
            temp.setVat(oe.getMomsPrecentage());
            temp.setCreationdate(i.getFakturaDate());
            temp.setExpiredate(i.getExpireDate());
            
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
            invoicePage = invoiceService.findByCustomer(uh.getUA(), c, terms);
        } else {
            invoicePage = invoiceService.findByUser(uh.getUA(), terms);
        }

        size = invoicePage.getTotalElements();
        return size;
    }
}
