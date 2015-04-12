/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.data.domain.Page;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;

/**
 *
 * @author Jakob
 */
public interface ProductService {
    
    void save(Product product);
    
    void remove(Product product);
    
    Product findProductById(UserAccount user, Long id);
    
    Page<Product> findAllProducts(UserAccount user, PagingAndSortingTerms terms);
}
