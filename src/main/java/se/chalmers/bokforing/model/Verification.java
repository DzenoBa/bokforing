/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
@Table(name = "Verifications")
public class Verification implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private UserAccount userAccount;

    @Column(nullable = false)
    private Long verificationNumber;

    @OneToMany(mappedBy = "verification", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Post> posts;

    /**
     * The date that the transaction which the verification concerns was made.
     */
    @Temporal(TemporalType.DATE)
    private Date transactionDate;

    /**
     * The date that this verification was created.
     */
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Customer customer;

    private String description;

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
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
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
        return "Verification{" + "id=" + getId() + ", posts=" + getPosts() + ", transactionDate=" + getTransactionDate() + ", creationDate=" + getCreationDate() + ", customer=" + getCustomer() + '}';
    }

    /**
     * @return the userAccount
     */
    public UserAccount getUserAccount() {
        return userAccount;
    }

    /**
     * @param userAccount the userAccount to set
     */
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    /**
     * @return the verificationNumber
     */
    public Long getVerificationNumber() {
        return verificationNumber;
    }

    /**
     * @param verificationNumber the verificationNumber to set
     */
    public void setVerificationNumber(Long verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.getUserAccount());
        hash = 59 * hash + Objects.hashCode(this.getVerificationNumber());
        hash = 59 * hash + Objects.hashCode(this.getPosts());
        hash = 59 * hash + Objects.hashCode(this.getTransactionDate());
        hash = 59 * hash + Objects.hashCode(this.getCreationDate());
        hash = 59 * hash + Objects.hashCode(this.getCustomer());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        final Verification other = (Verification) obj;
        if (!Objects.equals(this.userAccount, other.userAccount)) {
            return false;
        } else if (!Objects.equals(this.verificationNumber, other.verificationNumber)) {
            return false;
        } else if (!Objects.equals(this.transactionDate, other.transactionDate)) {
            return false;
        } else if (!Objects.equals(this.creationDate, other.creationDate)) {
            return false;
        } else if (!Objects.equals(this.customer, other.customer)) {
            return false;
        }
        return true;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
