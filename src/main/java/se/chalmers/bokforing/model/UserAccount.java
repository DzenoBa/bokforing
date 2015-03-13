/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import se.chalmers.bokforing.util.PasswordUtil;

/**
 *
 * @author victor
 */
@Entity
public class UserAccount implements Serializable {
  
    @Id
    @GeneratedValue
    private Long id;
    
    /** Name of the user */
    @Column(unique = true)
    private String name;
    
    /** The password of the user */
    private String pass;
    
    private String salt = PasswordUtil.randomString(8);
    
    /** The unique email of the users */
    @Column(unique = true)
    private String email;
    
    //Group is a reserved word. We cannot have it as a column name.   
    @Enumerated(EnumType.STRING)
    private UserGroup userGroup;
   
    private String sessionid;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastLogIn;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Verification> verifications;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Customer> customers;

    public Long getId(){
        return id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
    public void setPass(String pass) {
        this.pass = pass;
    }
    
    /**
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
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
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the group
     */
    public UserGroup getGroup() {
        return getUserGroup();
    }

    /**
     * @param group the group to set
     */
    public void setGroup(UserGroup group) {
        this.setUserGroup(group);
    }

    /**
     * @return String session id
     */
    public String getSessionid() {
        return sessionid;
    }
    
    /**
     * @param sessionid 
     */
    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
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
    
    @Override
    public String toString() {
        return "UserEnt{ id= " + getId() + "name=" + getName() + ", pass=" + getPass() 
                + ", salt= "+ getSalt() +", email=" + getEmail() 
                + ", group=" + getUserGroup() + ", sessionid=" + getSessionid()
                + ", lastLogIn=" + getLastLogIn() + '}';
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
    public void setVerifications(List<Verification> verifications) {
        this.verifications = verifications;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
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
    public void setUserGroup(UserGroup userGroup) {
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
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
