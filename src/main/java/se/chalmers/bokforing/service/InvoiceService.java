/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.data.domain.Page;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Invoice;
import se.chalmers.bokforing.model.UserInfo;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;

/**
 *
 * @author victor
 */
public interface InvoiceService {

    Invoice getById(Long id);
    
    void storeFaktura(Invoice fak);
    
    Page<Invoice> findAllInvoices(UserInfo userinfo, PagingAndSortingTerms terms);
    
    Page<Invoice> findByCustomer(UserInfo userinfo, Customer customer, PagingAndSortingTerms terms);
}
