/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.orders;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
@Entity
public class Faktura implements Serializable {
    @ManyToOne
    private OrderEntity orderEntity;
    
    public void setOrderEntity(OrderEntity oe){
        orderEntity = oe;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fakturaId;

    private boolean valid = true;
    //Date
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fakturaDate = new Date();
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expireDate = fakturaDate;
    
    //private Content content;
        
    private boolean fskatt = false;
    private String momsRegistredNumber;
    private Double momsPrecentage;    

    /**
     * @return the fakturaId
     */
    public Long getFakturaId() {
        return fakturaId;
    }

    /**
     * @param fakturaId the fakturaId to set
     */
    public void setFakturaId(Long fakturaId) {
        this.fakturaId = fakturaId;
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * @return the fromUser
     */
    public UserInfo getFromUser() {
        return orderEntity.getSeller();
    }

    /**
     * @param fromUser the fromUser to set
     */
    public void setFromUser(UserInfo fromUser) {
        orderEntity.setSeller(fromUser);
    }

    /**
     * @return the toUser
     */
    public Customer getToUser() {
        return orderEntity.getBuyer();
    }


    public void setToUser(Customer costumer) {
        orderEntity.setBuyer(costumer);
    }

    /**
     * @return the fakturaDate
     */
    public Date getFakturaDate() {
        return fakturaDate;
    }

    /**
     * @param fakturaDate
     */
    public void setFakturaDate(Date fakturaDate) {
        this.fakturaDate = fakturaDate;
    }

    /**
     * @return the expireDate
     */
    public Date getExpireDate() {
        return expireDate;
    }

    /**
     * @param expireDate the expireDate to set
     */
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    /**
     * @return the fskatt
     */
    public Boolean getFskatt() {
        return fskatt;
    }

    /**
     * @param fskatt the fskatt to set
     */
    public void setFskatt(Boolean fskatt) {
        this.fskatt = fskatt;
    }

    /**
     * @return the momsRegistredNumber
     */
    public String getMomsRegistredNumber() {
        return momsRegistredNumber;
    }

    /**
     * @param momsRegistredNumber the momsRegistredNumber to set
     */
    public void setMomsRegistredNumber(String momsRegistredNumber) {
        this.momsRegistredNumber = momsRegistredNumber;
    }

    /**
     * @return the momsPrecentage
     */
    public Double getMomsPrecentage() {
        return momsPrecentage;
    }

    /**
     * @param momsPrecentage the momsPrecentage to set
     */
    public void setMomsPrecentage(Double momsPrecentage) {
        this.momsPrecentage = momsPrecentage;
    }
}
