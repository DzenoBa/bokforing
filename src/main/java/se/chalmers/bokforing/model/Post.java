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
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private PostSum postSum;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Verification verification;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    /**
     * It it necessary to be able to tell if a Post has been corrected. See 5
     * kap. 5 § BFL
     */
    private boolean correction;

    /**
     * So we know which Posts to use for validation.
     */
    private boolean active;

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
        hash = 47 * hash + Objects.hashCode(this.getPostSum());
        hash = 47 * hash + Objects.hashCode(this.getAccount());
        hash = 47 * hash + Objects.hashCode(this.getVerification());
        hash = 47 * hash + Objects.hashCode(this.getCreationDate());
        hash = 47 * hash + (this.isCorrection() ? 1 : 0);
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
        if (this.isCorrection() != other.isCorrection()) {
            return false;
        }
        if (this.isActive() != other.isActive()) {
            return false;
        }
        return true;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public double getBalance() {
        if (!isActive()) { // we only care about ones that haven't been replaced
            return 0;
        }

        double debitAccountTypeFactor = 0;
        double creditAccountTypeFactor = 0;

        // Different account types count the debit and credit sides of the
        // post differently. Some make credit negative, some make credit 
        // positive etc.
        switch (account.getAccountType()) {
            case ASSETS:    // 1
            case MATERIAL_AND_PRODUCT_COSTS: // 4
            case COSTS_5: // 5
            case COSTS_6: // 6
            case COSTS_7: // 7
            case COSTS_8:
                debitAccountTypeFactor = 1;
                creditAccountTypeFactor = -1;
                break;
            case FUNDS_AND_DEBT: // 2
            case REVENUE: // 3
                debitAccountTypeFactor = -1;
                creditAccountTypeFactor = 1;
                break;
        }

        double balance = 0;

        if (postSum != null && postSum.getType() != null) {
            switch (postSum.getType()) {
                case Credit:
                    balance += postSum.getSumTotal() * creditAccountTypeFactor;
                    break;
                case Debit:
                    balance += postSum.getSumTotal() * debitAccountTypeFactor;
                    break;
            }
        }

        return balance;
    }

    public double getBalanceIgnoreSign() {
        if (!isActive()) { // we only care about ones that haven't been replaced
            return 0;
        }

        double balance = 0;

        if (postSum != null && postSum.getType() != null) {
            switch (postSum.getType()) {
                case Credit:
                    balance -= postSum.getSumTotal();
                    break;
                case Debit:
                    balance += postSum.getSumTotal();
                    break;
            }
        }

        return balance;
    }
}
