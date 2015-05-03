/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.List;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.orders.Invoice;
import se.chalmers.bokforing.model.orders.OrderEntity;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
public interface InvoiceService {
    Invoice getById(Long id);
    
    List<Invoice> getByOrderEntity(OrderEntity oe);
    
    void storeFaktura(Invoice fak);
}
