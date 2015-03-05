/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    private int id;
    
    /** Name of the user */
    private String name;
    
    /** The password of the user */
    private String pass;
    
    private String salt = PasswordUtil.randomString(8);;
    
    @Column(unique = true)
    /** The unique email of the users */
    private String email;
    
    //Group is a reserved word. We cannot have it as a column name.   
    @Enumerated(EnumType.STRING)
    private UserGroup userGroup;
   
    private String sessionid;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastLogIn;

    public int getId(){
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
        return userGroup;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(UserGroup group) {
        this.userGroup = group;
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
        return "UserEnt{ id= " + id + "name=" + name + ", pass=" + pass 
                + ", salt= "+ salt +", email=" + email 
                + ", group=" + userGroup + ", sessionid=" + sessionid
                + ", lastLogIn=" + lastLogIn + '}';
    }
}
