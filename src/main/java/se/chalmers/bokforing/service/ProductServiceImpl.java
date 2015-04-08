/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.beans.factory.annotation.Autowired;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.persistence.ProductRepository;

/**
 *
 * @author Jakob
 */
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;
    
    @Override
    public void save(Product product) {
        if(product != null) {
            repository.save(product);
        }
    }

    @Override
    public void remove(Product product) {
        if(product != null) {
            repository.delete(product);
        }
    }
    
}
