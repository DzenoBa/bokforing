/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    
    /** It it necessary to be able to tell if a post has been corrected.
     * See 5 kap. 5 § BFL */
    private boolean correction;

    
    /**
     * @return the postSum
     */
    public PostSum getPostSum() {
        return postSum;
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
     * @return the correction
     */
    public boolean isCorrection() {
        return correction;
    }

    /**
     * @param correction the correction to set
     */
    public void setCorrection(boolean correction) {
        this.correction = correction;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.postSum);
        hash = 47 * hash + Objects.hashCode(this.account);
        hash = 47 * hash + Objects.hashCode(this.verification);
        hash = 47 * hash + Objects.hashCode(this.creationDate);
        hash = 47 * hash + (this.correction ? 1 : 0);
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
        if (!Objects.equals(this.postSum, other.postSum)) {
            return false;
        }
        if (!Objects.equals(this.account, other.account)) {
            return false;
        }
        if (!Objects.equals(this.verification, other.verification)) {
            return false;
        }
        if (!Objects.equals(this.creationDate, other.creationDate)) {
            return false;
        }
        if (this.correction != other.correction) {
            return false;
        }
        return true;
    }
}
