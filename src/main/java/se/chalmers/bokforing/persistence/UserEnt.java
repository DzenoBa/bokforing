/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author victor
 */
@Entity
public class UserEnt implements Serializable {
  
    /** Name of the user */
    private String name;
    
    /** The password of the user */
    private String pass;
    
    @Id
    @Column(unique = true)
    /** The unique email of the users */
    private String email;
    
    //Group is a reserved word. We cannot have it as a column name.   
    @Column(name="group2")
    @Enumerated(EnumType.STRING)
    private Group group;
   
    private String sessionid;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastLogIn;

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
    public Group getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(Group group) {
        this.group = group;
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
        return "UserEnt{ name=" + name + ", pass=" + pass + ", email=" + email 
                + ", group=" + group + ", sessionid=" + sessionid
                + ", lastLogIn=" + lastLogIn + '}';
    }
}
