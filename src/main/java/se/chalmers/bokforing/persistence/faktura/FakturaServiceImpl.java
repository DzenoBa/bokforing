/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence.faktura;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import se.chalmers.bokforing.model.faktura.Faktura;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.model.user.UserInfo;

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
    public List<Faktura> findByFromUser(UserInfo fromUser) {
        return frep.findByFromUser(fromUser);
    }

    @Override
    public List<Faktura> findByToUser(UserInfo toUser) {
        return frep.findByToUser(toUser);
    }
    
        @Override
    public List<Faktura> findByFromUser(UserHandler fromUser) {
        return frep.findByFromUser(fromUser.getUI());
    }

    @Override
    public List<Faktura> findByToUser(UserHandler toUser) {
        return frep.findByToUser(toUser.getUI());
    }

    @Override
    public void storeFaktura(Faktura fak) {
        frep.save(fak);
    }
    
}
