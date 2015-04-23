
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
import se.chalmers.bokforing.jsonobject.AccountJSON;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.ProductJSON;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.service.ProductManager;
import se.chalmers.bokforing.service.ProductService;
import se.chalmers.bokforing.session.AuthSession;
import se.chalmers.bokforing.util.Constants;

/**
 *
 * @author Dženan
 */
@Controller
public class ProductController {
    
    @Autowired 
    private AuthSession authSession;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductManager productManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AccountService accountService;
    
    /*
     * CREATE PRODUCT
     */
    @RequestMapping(value = "/product/create", method = RequestMethod.POST)
    public @ResponseBody FormJSON create(@RequestBody final ProductJSON product) {
        
        FormJSON form = new FormJSON();
        
        // CHECK SESSION
        if(!authSession.sessionCheck()) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();
        
        // CHECK NAME
        if(product.getName() == null || product.getName().isEmpty()) {
            form.addError("name", "Vänligen ange ett namn.");
            return form;
        }
        
        String description = "";
        if(product.getDescription() != null) {
            description = product.getDescription();
        }
        
        // PRICE CHECK
        if(!(product.getPrice() > 0)) {
            form.addError("price", "Priset måste vara en positiv summa.");
            return form;
        }
        
        // QUANNTITY-TYPE CHECK
        if(product.getQuantitytype() == null || product.getQuantitytype().isEmpty()) {
            form.addError("quantitytype", "Vänligen ange en enhet");
            return form;
        }
        
        // ACCOUNT CHECK
        if(product.getAccount() == null || !(product.getAccount().getNumber() > 0)) {
            form.addError("account", "Vänligen ange ett konto");
            return form;
        }
        
        // VAT-ACCOUNT CHECK
        if(product.getVat() == null || !(product.getVat().getNumber() > 0)) {
            form.addError("account", "Vänligen ange moms");
            return form;
        }
        
        Account account = accountService.findAccountByNumber(product.getAccount().getNumber());
        Account vat_account = accountService.findAccountByNumber(product.getVat().getNumber());
        
        if(account == null || vat_account == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER
        UserHandler uh = userService.getUser(email);
        Product pDb = productManager.createProduct(uh.getUA(), product.getName(), 
                product.getPrice(), Product.QuantityType.valueOf(product.getQuantitytype()), description, account, vat_account);
        if(pDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
        }
        return form;
    }
    
    /*
     * GET PRODUCT
     */
    @RequestMapping(value = "/product/getproducts", method = RequestMethod.POST)
    public @ResponseBody List<ProductJSON> getProducts(@RequestBody final ProductJSON product) {
        
        List<ProductJSON> productJSONLs = new ArrayList();
        int startPos = 0;
        int pageSize = Constants.DEFAULT_PAGE_SIZE;
        
        if(!authSession.sessionCheck()) {
            return productJSONLs;
        }
        UserHandler ua = userService.getUser(authSession.getEmail());
        
        if(product.getStartrange() > 0) {
            startPos = product.getStartrange();
        }
        if(product.getPagesize() > 0) {
            pageSize = product.getPagesize();
        }
        
        PagingAndSortingTerms terms = new PagingAndSortingTerms(startPos, Boolean.TRUE, "name", pageSize);
        Page<Product> productPage;
        if(product.getName() != null && !product.getName().isEmpty()) {
            productPage = productService.findByNameLike(ua.getUA(), product.getName(), terms);
        } else {
            productPage = productService.findAllProducts(ua.getUA(), terms);
        }
        
        for(Product p : productPage.getContent()) {
            ProductJSON temp = new ProductJSON();
            temp.setId(p.getId());
            temp.setName(p.getName());
            temp.setPrice(p.getPrice());
            temp.setQuantitytype(p.getQuantityType().toString());
            temp.setDescription(p.getDescription());
            
            AccountJSON temp_a = new AccountJSON();
            temp_a.setNumber(p.getDefaultAccount().getNumber());
            temp_a.setName(p.getDefaultAccount().getName());
            temp.setAccount(temp_a);
            
            if(p.getVATAccount() != null) {
                AccountJSON temp_a_vat = new AccountJSON();
                temp_a_vat.setNumber(p.getVATAccount().getNumber());
                temp_a_vat.setName(p.getVATAccount().getName());
                temp.setVat(temp_a_vat);
            }
            
            productJSONLs.add(temp);
        }
        
        return productJSONLs;
    }
    
    /*
     * COUNT PRODUCT
     */
    @RequestMapping(value = "/product/countproducts", method = RequestMethod.POST)
    public @ResponseBody long countProducts(@RequestBody final ProductJSON product) {
        
        long size = 0;
        int pageSize = Constants.DEFAULT_PAGE_SIZE;
        
        if(!authSession.sessionCheck()) {
            return size;
        }
        UserHandler ua = userService.getUser(authSession.getEmail());

        if(product.getPagesize() > 0) {
            pageSize = product.getPagesize();
        }
        
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.TRUE, "name", pageSize);
        Page<Product> productPage;
        if(product.getName() != null && !product.getName().isEmpty()) {
            productPage = productService.findByNameLike(ua.getUA(), product.getName(), terms);
        } else {
            productPage = productService.findAllProducts(ua.getUA(), terms);
        }
        
        size = productPage.getTotalElements();
        return size;
    }
    
    /*
     * EDIT PRODUCT
     */
    @RequestMapping(value = "/product/edit", method = RequestMethod.POST)
    public @ResponseBody FormJSON edit(@RequestBody final ProductJSON product) {
        
        FormJSON form = new FormJSON();
        
        // CHECK SESSION
        if(!authSession.sessionCheck()) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();
        
        // CHECK ID
        if(product.getId() == null || product.getId() <= 0) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
        
        // CHECK NAME
        if(product.getName() == null || product.getName().isEmpty()) {
            form.addError("name", "Vänligen ange ett namn.");
            return form;
        }
        
        String description = "";
        if(product.getDescription() != null) {
            description = product.getDescription();
        }
        
        // PRICE CHECK
        if(!(product.getPrice() > 0)) {
            form.addError("price", "Vänligen ange ett korrekt pris.");
            return form;
        }
        
        // QUANNTITY-TYPE CHECK
        if(product.getQuantitytype() == null || product.getQuantitytype().isEmpty()) {
            form.addError("quantitytype", "Vänligen ange en enhet.");
            return form;
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER
        UserHandler uh = userService.getUser(email);
        Product pDb = productService.findProductById(uh.getUA(), product.getId());
        if(pDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
        } else {
            pDb.setName(product.getName());
            pDb.setPrice(product.getPrice());
            pDb.setQuantityType(Product.QuantityType.valueOf(product.getQuantitytype()));
            pDb.setDescription(description);

            productService.save(pDb);
        }
        
        return form;
    }
    
    /*
     * DELETE PRODUCT
     */
    @RequestMapping(value = "/product/delete", method = RequestMethod.POST)
    public @ResponseBody FormJSON delete(@RequestBody final ProductJSON product) {
        
        FormJSON form = new FormJSON();
        
        // CHECK SESSION
        if(!authSession.sessionCheck()) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();
        
        // CHECK ID
        if(product.getId() == null || product.getId() <= 0) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
              
        // EVERYTHING SEEMS TO BE IN ORDER
        UserHandler uh = userService.getUser(email);
        Product pDb = productService.findProductById(uh.getUA(), product.getId());
        if(pDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }
        
        productService.remove(pDb);
        
        return form;
    }
    
    /*
     * GET QUANNTITY-TYPES
     */
    @RequestMapping(value = "/product/getquantitytypes", method = RequestMethod.GET)
    public @ResponseBody List<String> getQuantityTypes () {
        
        List<String> types = new ArrayList();
        
        Product.QuantityType[] QuantityTypeLs = Product.QuantityType.values();
        for(Product.QuantityType quantityType : QuantityTypeLs) {
            types.add(quantityType.toString());
        }
        
        return types;
    }
}
