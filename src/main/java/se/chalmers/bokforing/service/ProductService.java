/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.model.Product;

/**
 *
 * @author Jakob
 */
public interface ProductService {
    
    void save(Product product);
    
    void remove(Product product);
}
