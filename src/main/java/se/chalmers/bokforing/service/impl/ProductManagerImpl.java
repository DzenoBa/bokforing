/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.service.ProductManager;
import se.chalmers.bokforing.service.ProductService;

/**
 *
 * @author Jakob
 */
@Service
public class ProductManagerImpl implements ProductManager {

    @Autowired
    private ProductService productService;
    
    @Override
    public Product createProduct(UserAccount user, String name, double price, Product.QuantityType quantityType, String description, Account defaultAccount, Account VATAccount) {
        if(!productIsValid(user, name, price, quantityType, description, defaultAccount, VATAccount)) {
            return null;
        }
        
        Product product = new Product();
        product.setUserAccount(user);
        product.setName(name);
        product.setPrice(price);
        product.setQuantityType(quantityType);
        product.setDescription(description);
        product.setDefaultAccount(defaultAccount);
        product.setVATAccount(VATAccount);
        
        productService.save(product);
        
        return product;
    }

    @Override
    public void removeProduct(Product product) {
        if(product != null) {
            productService.remove(product);
        }
    }

    private boolean productIsValid(UserAccount user, String name, double price, Product.QuantityType quantityType, String description, Account defaultAccount, Account VATAccount) {
        // TODO: validation
        return true;
    }
    
}
