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
import javax.persistence.GenerationType;
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
protected UserInfo(){
    
}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userInfoId;
    @OneToOne
    private UserAccount ua;
    private String userName;
    private String phoneNumber;
    private String companyName;
    private String companyAdr;
    private String postCode;
    private String bankgiro;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastLogIn;
    private URI logo;

    /**
     * @return the UserInfo_Id
     */
    public Integer getUserInfoId() {
        return userInfoId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return userName;
    }
    void setName(String name){
        this.userName = name;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    void setPhoneNumber(String number){
        this.phoneNumber = number;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }
    void setCompanyName(String name){
        this.companyName = name;
    }

    public String getCompanyAdr() {
        return companyAdr;
    }
    void setCompanyAdr(String adr){
        this.companyAdr = adr;
    }
    
    public String getPostCode() {
        return postCode;
    }
    void setPostCode(String pcode){
        this.postCode = pcode;
    }
    
    /**
     * @return the logo
     */
    public URI getLogo() {
        return logo;
    }
    void setLogo(URI logo){
        this.logo = logo;
    }

    /**
     * @return the ua
     */
    public UserAccount getUa() {
        return ua;
    }
    void setUa(UserAccount ua){
        this.ua = ua;
    }
    
        /**
     * @return the lastLogIn
     */
    public Date getLastLogIn() {
        return lastLogIn;
    }
    void setLastLogIn(){
        setLastLogIn(new Date());
    }
    void setLastLogIn(Date date){
        lastLogIn = date;
    }
    
    void setBankgiro(String bankgiro){
        this.bankgiro = bankgiro;
    }
    public String getBankgiro(){
        return this.bankgiro;
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
        sb.append(" }");
        return  sb.toString();  
    }

}
