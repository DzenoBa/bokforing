
package se.chalmers.bokforing.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.ProductJSON;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.persistence.user.UserService;
import se.chalmers.bokforing.service.ProductManager;
import se.chalmers.bokforing.service.ProductService;
import se.chalmers.bokforing.session.AuthSession;

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
    
    /*
     * CREATE PRODUCT
     */
    @RequestMapping(value = "/product/create", method = RequestMethod.POST)
    public @ResponseBody FormJSON create(@RequestBody final ProductJSON product) {
        
        FormJSON form = new FormJSON();
        
        // CHECK SESSION
        if(!authSession.sessionCheck()) {
            form.addError("general", "Ett fel inträffades, du har inte rätt tillstånd för att utföra denna åtgärd!");
            return form;
        }
        String email = authSession.getEmail();
        
        // CHECK NAME
        if(product.getName() == null || product.getName().isEmpty()) {
            form.addError("name", "Vänligen ange ett namn");
            return form;
        }
        
        String description = "";
        if(product.getDescription() != null) {
            description = product.getDescription();
        }
        
        // PRICE CHECK
        if(!(product.getPrice() > 0)) {
            form.addError("price", "Vänligen ange ett korrekt pris");
            return form;
        }
        
        // QUANNTITY-TYPE CHECK
        if(product.getQuantitytype() == null || product.getQuantitytype().isEmpty()) {
            form.addError("quantitytype", "Vänligen ange en enhet");
            return form;
        }
        
        // EVERYTHING SEEMS TO BE IN ORDER
        UserHandler uh = userService.getUser(email);
        Product pDb = productManager.createProduct(uh.getUA(), product.getName(), 
                product.getPrice(), Product.QuantityType.valueOf(product.getQuantitytype()), description);
        if(pDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund!");
        }
        return form;
    }
    
    /*
     * GET PRODUCT
     */
    @RequestMapping(value = "/bookkeeping/getproducts", method = RequestMethod.POST)
    public @ResponseBody List<ProductJSON> getProducts(@RequestBody final String start) {
        
        List<ProductJSON> productJSONLs = new ArrayList();
        int startPos = 0;
        
        if(!authSession.sessionCheck()) {
            return productJSONLs;
        }
        UserHandler ua = userService.getUser(authSession.getEmail());
        
        if(Integer.parseInt(start) > 0) {
            startPos = Integer.parseInt(start);
        }
        
        PagingAndSortingTerms terms = new PagingAndSortingTerms(startPos, Boolean.TRUE, "name");
        //Page<Product> productPage = productService. // TODO
        
        return productJSONLs;
    }
    
    @RequestMapping(value = "/product/getquantitytype", method = RequestMethod.GET)
    public @ResponseBody List<String> getQuantityTypes () {
        
        List<String> types = new ArrayList();
        
        Product.QuantityType[] QuantityTypeLs = Product.QuantityType.values();
        for(Product.QuantityType quantityType : QuantityTypeLs) {
            types.add(quantityType.toString());
        }
        
        return types;
    }
}
