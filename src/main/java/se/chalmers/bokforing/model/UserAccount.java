/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import se.chalmers.bokforing.helperfunctions.HelpY;

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
    @OneToOne
    @JoinColumn(name="userInfoId")
    private UserInfo userInfo;
    
    /** The password of the user */
    private String pass;
    
    private String salt = HelpY.randomString(8);;
    
    @Column(unique = true)
    /** The unique email of the users */
    private String email;
    
    //Group is a reserved word. We cannot have it as a column name.   
    @Enumerated(EnumType.STRING)
    private UserGroup userGroup;
   
    private String sessionid;

    public int getId(){
        return id;
    }
    
    public void setUserInfo(UserInfo userInfo){
        this.userInfo = userInfo;
    }
    /**
     * @return the name
     */
    public UserInfo getUseInfo() {
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
        return  sb.toString();
    }
}
