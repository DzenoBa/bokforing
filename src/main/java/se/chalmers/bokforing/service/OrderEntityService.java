/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.List;
import org.springframework.data.domain.Page;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.orders.OrderEntity;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.model.UserInfo;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;

/**
 *
 * @author victor
 */
public interface OrderEntityService {
    public OrderEntity getById(Long id);
    
    Page<OrderEntity> findByFromUser(UserInfo fromUser, PagingAndSortingTerms terms);
    
    Page<OrderEntity> findByToUser(Customer toUser, PagingAndSortingTerms terms);
    
    Page<OrderEntity> findByFromUser(UserHandler fromUser, PagingAndSortingTerms terms);
    
    public void storeOrderEntity(OrderEntity oe);
    
    public int generateInvoice(OrderEntity oe);
    
    public void invalidate(OrderEntity oe);
}
