/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.model.Invoice;
import se.chalmers.bokforing.model.OrderEntity;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;

/**
 *
 * @author victor
 */
public interface InvoiceService {
    Invoice getById(Long id);
    
    Page<Invoice> getByOrderEntity(OrderEntity oe, PagingAndSortingTerms terms);
    
    Page<Invoice> findByCustomer(UserAccount user, Customer customer, PagingAndSortingTerms terms);
    
    void storeFaktura(Invoice fak);
}
