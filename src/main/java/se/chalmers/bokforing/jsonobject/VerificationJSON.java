
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
    private double sum;
    private Date transactionDate;
    private Date creationDate;
    
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
}
