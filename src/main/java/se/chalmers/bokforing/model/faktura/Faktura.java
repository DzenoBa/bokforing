/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.faktura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
@Entity
public class Faktura implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fakturaId;

    private boolean valid = true;
        
    //From
    //We need names, phone numbers and company from the info.
    @ManyToOne
    private UserInfo fromUser;
    
    //To
    @ManyToOne
    private UserInfo toUser;
    
    //Date
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fakturaDatum;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expireDate;
    
    private ArrayList<Content> content;
    
    private Float totalCost;
    
    private String fskatt;
    private String momsRegistredNumber;
    private Float momsCost;
    private Float momsPrecentage;    

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
        return fromUser;
    }

    /**
     * @param fromUser the fromUser to set
     */
    public void setFromUser(UserInfo fromUser) {
        this.fromUser = fromUser;
    }

    /**
     * @return the toUser
     */
    public UserInfo getToUser() {
        return toUser;
    }

    /**
     * @param toUser the toUser to set
     */
    public void setToUser(UserInfo toUser) {
        this.toUser = toUser;
    }

    /**
     * @return the fakturaDatum
     */
    public Date getFakturaDatum() {
        return fakturaDatum;
    }

    /**
     * @param fakturaDatum the fakturaDatum to set
     */
    public void setFakturaDatum(Date fakturaDatum) {
        this.fakturaDatum = fakturaDatum;
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
     * @return the content
     */
    public ArrayList<Content> getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(ArrayList<Content> content) {
        this.content = content;
    }

    /**
     * @return the totalCost
     */
    public Float getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(Float totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return the fskatt
     */
    public String getFskatt() {
        return fskatt;
    }

    /**
     * @param fskatt the fskatt to set
     */
    public void setFskatt(String fskatt) {
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
     * @return the momsCost
     */
    public Float getMomsCost() {
        return momsCost;
    }

    /**
     * @param momsCost the momsCost to set
     */
    public void setMomsCost(Float momsCost) {
        this.momsCost = momsCost;
    }

    /**
     * @return the momsPrecentage
     */
    public Float getMomsPrecentage() {
        return momsPrecentage;
    }

    /**
     * @param momsPrecentage the momsPrecentage to set
     */
    public void setMomsPrecentage(Float momsPrecentage) {
        this.momsPrecentage = momsPrecentage;
    }
}
