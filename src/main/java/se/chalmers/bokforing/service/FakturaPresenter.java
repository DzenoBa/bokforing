/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.model.user.Faktura;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
public class FakturaPresenter {
    final Faktura fak;
    final UserInfo to;
    final UserInfo from;
    
    public FakturaPresenter(Faktura faktura){
        this.fak = faktura;
        this.to = fak.getToUser();
        this.from = fak.getFromUser();
    }
    
}
