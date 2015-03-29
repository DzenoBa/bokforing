/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Isabelle
 */
@Entity
@Table(name = "Posts")
public class Post implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @OneToOne(cascade = CascadeType.ALL)
    private PostSum postSum;
    
    @ManyToOne
    private Account account;
    
    @ManyToOne
    private Verification verification;

    
    /**
     * @return the postSum
     */
    public PostSum getPostSum() {
        return postSum;
    }

    /**
     * @param postSum the postSum to set
     */
    public void setSum(PostSum postSum) {
        this.setPostSum(postSum);
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

    @Override
    public String toString() {
        return "Post{" + "id=" + getId() + ", postSum=" + getPostSum() + ", account=" + getAccount() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.getId();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Post other = (Post) obj;
        if (this.getId() != other.getId()) {
            return false;
        }
        return true;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param postSum the postSum to set
     */
    public void setPostSum(PostSum postSum) {
        this.postSum = postSum;
    }

    /**
     * @return the verification
     */
    public Verification getVerification() {
        return verification;
    }

    /**
     * @param verification the verification to set
     */
    public void setVerification(Verification verification) {
        this.verification = verification;
    }
}
