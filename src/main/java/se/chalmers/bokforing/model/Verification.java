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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Swedish law about verifications, see especially §6.
 * http://www.riksdagen.se/sv/Dokument-Lagar/Lagar/Svenskforfattningssamling/
 * Bokforingslag-19991078_sfs-1999-1078/?bet=1999:1078#K5
 * 
 * @author Isabelle
 */
@Entity
public class Verification implements Serializable {
    
    @Id
    private String id;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Post> posts;

    /** The date that the transaction which the verification concerns was made. */
    @Temporal(TemporalType.DATE)
    private Date transactionDate;
    
    /** The date that this verification was created. */
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;
    
    /**
     * @return the posts
     */
    public List<Post> getPosts() {
        return posts;
    }

    /**
     * @param posts the posts to set
     */
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the transactionDate
     */
    public Date getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate the transactionDate to set
     */
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Verification{" + "id=" + id + ", posts=" + posts + ", transactionDate=" + transactionDate + ", creationDate=" + creationDate + ", customer=" + customer + '}';
    }
    
    
}
