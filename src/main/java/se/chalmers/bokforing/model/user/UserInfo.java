/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.user;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import javax.persistence.Column;
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
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @return the logo
     */
    public URI getLogo() {
        return logo;
    }

    /**
     * @return the ua
     */
    public UserAccount getUa() {
        return ua;
    }
    
        /**
     * @return the lastLogIn
     */
    public Date getLastLogIn() {
        return lastLogIn;
    }
    
    String toStringLight(){
        return userInfoId + ":" + userName;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("UserInfo{ id= ").append(userInfoId);
        sb.append(", userAccount=");
        if(ua != null)
            sb.append(ua.toStringLight());
        else
            sb.append("null");
        sb.append(", username=").append(userName);
        sb.append(", phonenumber= ").append(phoneNumber);
        sb.append(", companyname=").append(companyName);
        sb.append(", lastLogIn=").append(lastLogIn);
        sb.append(", logo=").append(logo);
        return  sb.toString();  
    }

}
