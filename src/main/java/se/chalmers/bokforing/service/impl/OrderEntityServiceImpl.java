/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.orders.OrderEntity;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.model.user.UserInfo;
import se.chalmers.bokforing.persistence.orders.OrderEntityRepository;
import se.chalmers.bokforing.service.OrderEntityService;

/**
 *
 * @author victor
 */
@Service
public class OrderEntityServiceImpl implements OrderEntityService {
    
    @Autowired
    private OrderEntityRepository oeRep;
    
    @Override
    public OrderEntity getById(Long id) {
        return oeRep.findByOrderEntityId(id);
    }

    @Override
    public List<OrderEntity> findByFromUser(UserInfo fromUser) {
        return oeRep.findBySeller(fromUser);
    }

    @Override
    public void storeOrderEntity(OrderEntity fak) {
        oeRep.save(fak);
    }

    @Override
    public List<OrderEntity> findByToUser(Customer toUser) {
        return oeRep.findByBuyer(toUser);
    }

    @Override
    public List<OrderEntity> findByFromUser(UserHandler fromUser) {
        return oeRep.findBySeller(fromUser.getUI());
    }
    
}
