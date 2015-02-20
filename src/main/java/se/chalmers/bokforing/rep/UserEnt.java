/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.rep;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author victor
 */
@Entity
public class UserEnt implements Serializable{
    @Id
    @GeneratedValue
    private Integer id;
    
    private Integer sesId;
    private String name;
    private String pass;
    private String email;
    //Group is a taken word. We cannot have it as a column name.
    private String group2;//Group String? Maybe we should define our own Group class. But string will do for now
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastLogIn;
    

    void updateDate(){
	Calendar calendar = new GregorianCalendar();
        setLastLogIn(calendar.getTime());
    }
    Date getDate(){
        return getLastLogIn();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: " + getId().toString() + "\n");
        sb.append("Session Id: " + getSesId().toString() + "\n");
        sb.append("Name: " + getName() + "\n");
        sb.append("Email: " + getEmail() + "\n");
        sb.append("Group: " + getGroup2() + "\n");
        sb.append("Last login: " + getLastLogIn().toString());
        return sb.toString();
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the sesId
     */
    public Integer getSesId() {
        return sesId;
    }

    /**
     * @param sesId the sesId to set
     */
    public void setSesId(Integer sesId) {
        this.sesId = sesId;
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
     * @return the group2
     */
    public String getGroup2() {
        return group2;
    }

    /**
     * @param group2 the group2 to set
     */
    public void setGroup2(String group2) {
        this.group2 = group2;
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
    
}
