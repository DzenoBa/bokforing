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
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Invoice;
import se.chalmers.bokforing.model.UserInfo;
import se.chalmers.bokforing.persistence.InvoiceRepository;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.InvoiceService;

/**
 *
 * @author victor
 */
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository frep;

    @Override
    public Invoice getById(Long id) {
        return frep.findByFakturaId(id);
    }

    @Override
    public void storeFaktura(Invoice fak) {
        frep.save(fak);
    }

/*    public Page<Invoice> getByOrderEntity(OrderEntity oe, PagingAndSortingTerms terms) {
        PageRequest request = terms.getPageRequest();

        return frep.findByOrderEntity(oe, request);
    }*/

    @Override
    public Page<Invoice> findByCustomer(UserInfo userinfo, Customer customer, PagingAndSortingTerms terms) {
        PageRequest request = terms.getPageRequest();

        return frep.findBySellerAndBuyer(userinfo, customer, request);

    }
    
    @Override
    public Page<Invoice> findAllInvoices(UserInfo userinfo, PagingAndSortingTerms terms) {
        PageRequest request = terms.getPageRequest();
        
        return frep.findBySeller(userinfo, request);
    }

}
