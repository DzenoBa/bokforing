/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * More information about the user.
 *
 * @author victor
 */
@Entity
public class UserInfo implements Serializable {

    @Id
    @GeneratedValue
    private int userInfoId;
    @OneToOne
    private UserAccount ua;
    private String userName;
    private String phoneNumber;
    private String companyName;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastLogIn;
    private URI logo;

    /**
     * @return the UserInfo_Id
     */
    public int getUserInfoId() {
        return userInfoId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return userName;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.userName = name;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the logo
     */
    public URI getLogo() {
        return logo;
    }

    /**
     * @param logo the logo to set
     */
    public void setLogo(URI logo) {
        this.logo = logo;
    }

    /**
     * @return the ua
     */
    public UserAccount getUa() {
        return ua;
    }

    /**
     * @param ua the ua to set
     */
    public void setUa(UserAccount ua) {
        this.ua = ua;
    }
    
        /**
     * @return the lastLogIn
     */
    public Date getLastLogIn() {
        return lastLogIn;
    }

    /**
     * @param lastLogIn the lastLogIn to set
     */
    public void setLastLogIn(Date lastLogIn) {
        this.lastLogIn = lastLogIn;
    }
    
    String toStringLight(){
        return userInfoId + ":" + userName;
    }
    
    @Override
    public String toString(){
        return toStringLight();
    }

}
