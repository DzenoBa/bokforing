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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author victor
 */
@Entity
public class UserEnt implements Serializable {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Integer id;

    private Integer sesId;
    private String name;
    private String pass;
    @Column(unique = true)
    private String email;
    //Group is a taken word. We cannot have it as a column name.
    @Column(name="group2")
    private String group;//Group String? Maybe we should define our own Group class. But string will do for now
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
    public String getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(String group) {
        this.group = group;
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
        return "UserEnt{" + "id=" + id + ", sesId=" + sesId + ", name=" + name + ", pass=" + pass + ", email=" + email + ", group2=" + group + ", lastLogIn=" + lastLogIn + '}';
    }
}
