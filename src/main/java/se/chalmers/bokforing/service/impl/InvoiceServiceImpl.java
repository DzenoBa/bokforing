/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.orders.Invoice;
import se.chalmers.bokforing.model.orders.OrderEntity;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.model.user.UserInfo;
import se.chalmers.bokforing.persistence.orders.InvoiceRepository;
import se.chalmers.bokforing.service.InvoiceService;

/**
 *
 * @author victor
 */
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

    @Override
    public List<Invoice> getByOrderEntity(OrderEntity oe) {
     return frep.findByOrderEntity(oe);
    }
    
}
