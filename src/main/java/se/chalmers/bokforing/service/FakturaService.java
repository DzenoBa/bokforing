/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.List;
import se.chalmers.bokforing.model.Faktura;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.model.UserInfo;

/**
 *
 * @author victor
 */
public interface FakturaService {
    public Faktura getById(Long id);
    
    List<Faktura> findByFromUser(UserInfo fromUser);
    List<Faktura> findByToUser(UserInfo toUser);
    
    List<Faktura> findByFromUser(UserHandler fromUser);
    List<Faktura> findByToUser(UserHandler toUser);
    
    public void storeFaktura(Faktura fak);
}
