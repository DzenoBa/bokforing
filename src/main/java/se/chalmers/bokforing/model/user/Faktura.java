/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author victor
 */
@Entity
public class Faktura implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long fakturaId;

    boolean valid = true;
        
    //From
    //We need names, phone numbers and company from the info.
    @ManyToOne
    UserInfo fromUser;
    
    //To
    @ManyToOne
    UserInfo toUser;
    
    //Date
    @Temporal(javax.persistence.TemporalType.DATE)
    Date fakturaDatum;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date expireDate;
    
    List<Content> content;
    
    Float totalCost;
    
    String fskatt;
    String momsRegistredNumber;
    Float momsCost;
    Float momsPrecentage;    
}
