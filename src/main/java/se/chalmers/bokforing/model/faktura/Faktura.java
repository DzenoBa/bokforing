/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.faktura;

import java.io.Serializable;
import java.util.Date;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
//@Entity
public class Faktura implements Serializable {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fakturaId;

    private boolean valid = true;
        
    //From
    //We need names, phone numbers and company from the info.
    //@ManyToOne
    private UserAccount fromUser;
    
    //To
    //@ManyToOne
    private UserAccount toUser;
    
    //Date
    //@Temporal(javax.persistence.TemporalType.DATE)
    private Date fakturaDate = new Date();
    //@Temporal(javax.persistence.TemporalType.DATE)
    private Date expireDate = fakturaDate;
    
    private Content content;
        
    private Boolean fskatt;
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
    public UserAccount getFromUser() {
        return fromUser;
    }

    /**
     * @param fromUser the fromUser to set
     */
    public void setFromUser(UserAccount fromUser) {
        this.fromUser = fromUser;
    }

    /**
     * @return the toUser
     */
    public UserAccount getToUser() {
        return toUser;
    }

    /**
     * @param toUser the toUser to set
     */
    public void setToUser(UserAccount toUser) {
        this.toUser = toUser;
    }

    /**
     * @return the fakturaDate
     */
    public Date getFakturaDate() {
        return fakturaDate;
    }

    /**
     * @param fakturaDatum the fakturaDate to set
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
     * @return the content
     */
    public Content getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(Content content) {
        this.content = content;
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
