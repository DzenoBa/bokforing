/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Isabelle
 */
@Entity
public class Post implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Sum sum;
    private Account account;
    
    @Temporal(TemporalType.DATE)
    private Date date;
    
    public Post(Date date, Sum sum, Account account){
        this.date = date;
        this.account = account;
        this.sum = sum;
    }

    /**
     * @return the sum
     */
    public Sum getSum() {
        return sum;
    }

    /**
     * @param sum the sum to set
     */
    public void setSum(Sum sum) {
        this.sum = sum;
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
