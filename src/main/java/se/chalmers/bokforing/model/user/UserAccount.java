/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.user;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.util.PasswordUtil;

/**
 *
 * @author victor
 */
@Entity
public class UserAccount implements Serializable {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="userInfoId")
    private UserInfo userInfo;
    
    /** The password of the user */
    private String pass;
    
    private String salt = PasswordUtil.randomString(8);
    
    /** The unique email of the users */
    @Column(unique = true)
    private String email;
    
    //Group is a reserved word. We cannot have it as a column name.   
    @Enumerated(EnumType.STRING)
    private UserGroup userGroup = UserGroup.User;
   
    private String sessionid;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Verification> verifications;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Customer> customers;
    
    protected UserAccount(){
        
    }
    public Long getId(){
        return id;
    }
    
    void setUserInfo(UserInfo userInfo){
        this.userInfo = userInfo;
    }
    /**
     * @return the name
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    void setPass(String pass) {
        this.pass = pass;
    }
    
    void setSalt(String salt){
        this.salt = salt;
    }
    /**
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    void setEmail(String email) {
        email = email.toLowerCase();
        this.email = email;
    }

    /**
     * @return String session id
     */
    public String getSessionid() {
        return sessionid;
    }
    void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    /**
     * @return the verifications
     */
    public List<Verification> getVerifications() {
        return verifications;
    }

    /**
     * @param verifications the verifications to set
     */
    void setVerifications(List<Verification> verifications) {
        this.verifications = verifications;
    }

    /**
     * @param id the id to set
     */
    void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the userGroup
     */
    public UserGroup getUserGroup() {
        return userGroup;
    }

    /**
     * @param userGroup the userGroup to set
     */
    void setUserGroup(UserGroup userGroup) {
        if(userGroup == null)
            throw new IllegalArgumentException("null is not allowed");
        this.userGroup = userGroup;
    }

    /**
     * @return the customers
     */
    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * @param customers the customers to set
     */
    void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
    String toStringLight(){
        return id + ":" + email;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserEnt{ id= ").append(id);
        sb.append(", userInfo=");
        if(userInfo != null)
            sb.append(userInfo.toStringLight());
        else
            sb.append("null");
        sb.append(", pass=").append(pass);
        sb.append(", salt= ").append(salt);
        sb.append(", email=").append(email);
        sb.append(", group=").append(userGroup);
        sb.append(", sessionid=").append(sessionid);
        sb.append(" }");
        return  sb.toString();  
    }
    
    @Override
    public boolean equals(Object obj){
    if(obj == this)
            return true;
    else if((obj == null) || (obj.getClass() != this.getClass()))
        return false;
    else{
        UserAccount other = (UserAccount) obj; 
            return (id == null ? other.id == null : id.equals(other.id))
                && (email == null ? other.email == null : email.equals(other.email))
                && (pass == null ? other.pass == null : pass.equals(other.pass))
                && (salt == null ? other.salt == null : salt.equals(other.salt))
                && (sessionid == null ? other.sessionid == null : sessionid.equals(other.sessionid))
                && (userGroup == null ? other.userGroup == null : userGroup.equals(other.userGroup))
                //&& (userInfo == other.userInfo || (userInfo != null && userInfo.equals(other.userInfo)))
                //&& (verifications == null ? other.verifications == null : verifications.equals(other.verifications))
                //&& (customers == null ? other.customers == null : customers.equals(other.customers))
                ;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.userInfo);
        hash = 83 * hash + Objects.hashCode(this.pass);
        hash = 83 * hash + Objects.hashCode(this.salt);
        hash = 83 * hash + Objects.hashCode(this.email);
        hash = 83 * hash + Objects.hashCode(this.userGroup);
        hash = 83 * hash + Objects.hashCode(this.sessionid);
        hash = 83 * hash + Objects.hashCode(this.verifications);
        hash = 83 * hash + Objects.hashCode(this.customers);
        return hash;
    }
}
