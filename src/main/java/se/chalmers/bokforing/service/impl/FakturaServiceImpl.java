/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.orders.Faktura;
import se.chalmers.bokforing.model.orders.OrderEntity;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.model.user.UserInfo;
import se.chalmers.bokforing.persistence.orders.FakturaRepository;
import se.chalmers.bokforing.service.FakturaService;

/**
 *
 * @author victor
 */
public class FakturaServiceImpl implements FakturaService {
    @Autowired
    private FakturaRepository frep;
    
    @Override
    public Faktura getById(Long id) {
        return frep.findByFakturaId(id);
    }

    @Override
    public void storeFaktura(Faktura fak) {
        frep.save(fak);
    }

    @Override
    public List<Faktura> getByOrderEntity(OrderEntity oe) {
     return frep.findByOrderEntity(oe);
    }
    
}
