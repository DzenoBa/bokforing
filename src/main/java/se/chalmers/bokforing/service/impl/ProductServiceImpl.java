/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.persistence.ProductRepository;
import se.chalmers.bokforing.service.ProductService;

/**
 *
 * @author Jakob
 */
@Service
@Transactional
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
    
    @Override
    public Product findProductById(UserAccount user, Long id) {
        return repository.findByIdAndUserAccount(id, user);
    }
    
    @Override
    public Page<Product> findAllProducts(UserAccount user, PagingAndSortingTerms terms) {
        PageRequest request = terms.getPageRequest();
        
        return repository.findByUserAccount(user, request);
    }

    @Override
    public Page<Product> findByNameLike(UserAccount user, String name, PagingAndSortingTerms terms) {
        if(user == null || name == null || name.isEmpty() || terms == null) {
            return null;
        }
        
        PageRequest request = terms.getPageRequest();
        
        return repository.findByUserAccountAndNameContainingIgnoreCase(user, name, request);
    }
    
}
