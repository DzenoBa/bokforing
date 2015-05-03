/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.List;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.orders.OrderEntity;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
public interface OrderEntityService {
    public OrderEntity getById(Long id);
    
    List<OrderEntity> findByFromUser(UserInfo fromUser);
    List<OrderEntity> findByToUser(Customer toUser);
    
    List<OrderEntity> findByFromUser(UserHandler fromUser);
    
    public void storeOrderEntity(OrderEntity oe);
    
    public int generateInvoice(OrderEntity oe);
    
    public void invalidate(OrderEntity oe);
}
