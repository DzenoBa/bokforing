package se.chalmers.bokforing.jsonobject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Dženan
 */
public class VerificationJSON implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private List<PostJSON> posts;
    private List<PostJSON> oldposts;
    private List<PostJSON> debitposts;
    private List<PostJSON> creditposts;
    private double sum;
    private Date transactionDate;
    private Date creationDate;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PostJSON> getPosts() {
        return posts;
    }

    public void setPosts(List<PostJSON> posts) {
        this.posts = posts;
    }

    public List<PostJSON> getOldposts() {
        return oldposts;
    }

    public void setOldposts(List<PostJSON> oldposts) {
        this.oldposts = oldposts;
    }

    public List<PostJSON> getDebitposts() {
        return debitposts;
    }

    public void setDebitposts(List<PostJSON> debitposts) {
        this.debitposts = debitposts;
    }

    public List<PostJSON> getCreditposts() {
        return creditposts;
    }

    public void setCreditposts(List<PostJSON> creditposts) {
        this.creditposts = creditposts;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
