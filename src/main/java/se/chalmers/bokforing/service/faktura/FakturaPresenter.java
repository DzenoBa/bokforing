/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.faktura;

import java.util.Date;
import se.chalmers.bokforing.model.faktura.Content;
import se.chalmers.bokforing.model.faktura.Faktura;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
public class FakturaPresenter {
    private final Faktura fak;
    
    //The big list of privates
    //From
    private final String fName;
    private final String fComp;
    private final String mNumber;
    private final Boolean fTax;
    
    //To
    private final String tName;
    private final String tComp;
    
    private final Date fakDate;
    private final Date expireDate;
    
    private final Long fakId;
    
    private final Content cont;
    
    private final Double totalCost;
    
    private final Double moms;
    
    private final Double momsPrecentage;
    
    public FakturaPresenter(Faktura faktura){
        this.fak = faktura;
        UserInfo to = fak.getToUser();
        UserInfo fr = fak.getFromUser();
        
        fName = fr.getName();
        fComp = fr.getCompanyName();
        mNumber = fak.getMomsRegistredNumber();
        fTax = fak.getFskatt();
        
        tName = to.getName();
        tComp = to.getCompanyName();
        
        fakDate = fak.getFakturaDatum();
        expireDate = fak.getExpireDate();
        
        fakId = fak.getFakturaId();
        
        cont = fak.getContent();
        totalCost = cont.getTotalPrice();
        
        moms = fak.getMomsCost();
        momsPrecentage = fak.getMomsPrecentage();
        
    }
    
}
