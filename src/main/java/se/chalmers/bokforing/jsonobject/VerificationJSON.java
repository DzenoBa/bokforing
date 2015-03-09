
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
    
    private List<PostJSON> posts;
    private Date transactionDate;
    
    public List<PostJSON> getPosts() {
        return posts;
    }
    public void setPosts(List<PostJSON> posts) {
        this.posts = posts;
    }
    
    public Date getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
