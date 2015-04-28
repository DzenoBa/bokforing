/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.Product.QuantityType;
import se.chalmers.bokforing.model.user.UserAccount;

/**
 *
 * @author Jakob
 */
public interface ProductManager {
    
    Product createProduct(UserAccount user, String name, double price, QuantityType quantityType, String description, Account defaultAccount, Account VATAccount);
    
    void removeProduct(Product product);
    
}
