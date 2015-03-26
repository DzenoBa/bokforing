
package se.chalmers.bokforing.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import se.chalmers.bokforing.model.user.UserAccount;

/**
 *
 * @author Dženan
 */
@Entity
@Table(name = "AccessKeys")
public class AccessKey implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String accesskey;
    
    @Enumerated(EnumType.STRING)
    private AccessKeyType type;
    
    @OneToOne
    private UserAccount userAccount;
    
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAccesskey() {
        return accesskey;
    }
    public void setAccecsskey(String accesskey) {
        this.accesskey = accesskey;
    }
    
    public AccessKeyType getType() {
        return type;
    }
    public void setType(AccessKeyType type) {
        this.type = type;
    }
    
    public UserAccount getUserAccount() {
        return userAccount;
    }
    public void setUserAccount(UserAccount useraccount) {
        this.userAccount = useraccount;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
}
