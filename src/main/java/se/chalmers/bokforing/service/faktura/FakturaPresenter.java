/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.faktura;

import se.chalmers.bokforing.model.faktura.Faktura;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
public class FakturaPresenter {
    final Faktura fak;
    final UserInfo to;
    final UserInfo from;
    
    //The big list of privates
    private String fName;
    private String fComp;
    private String mNumber;
    private Boolean fTax;
    
    public FakturaPresenter(Faktura faktura){
        this.fak = faktura;
        this.to = fak.getToUser();
        this.from = fak.getFromUser();
    }
    
}
