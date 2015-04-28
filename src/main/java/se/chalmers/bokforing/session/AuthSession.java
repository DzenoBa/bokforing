
package se.chalmers.bokforing.session;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.service.UserService;

/**
 *
 * @author Dženan
 */
@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class AuthSession implements Serializable {
    
    @Autowired
    private UserService userDb;
    
    private boolean status = false;
    private String email;
    private String sessionid;
    private String level;
    
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSessionid() {
        return sessionid;
    }
    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
    
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    
    public void setSession(String email, String sessionid, String level) {
        status = true;
        this.email = email;
        this.sessionid = sessionid;
        this.level = level;
    }
    
    public void clearSession() {
        status = false;
        email = null;
        sessionid = null;
        level = null;
    }
    
    /**
     * SESSION CHECK
     * 
     * Checks if the session is valid
     * @return boolean; true if user is online
     */
    public boolean sessionCheck() {
        
        // CHECK IF VALID USER
        if(getStatus()) {
            UserHandler u = userDb.getUser(getEmail());
            
            // CHECK IF USER EXIST
            if(u == null) {
                clearSession();
                return false;
            }
            
            // CHECK IF THE SESSION IS CORRECT
            if(!(u.getSessionid().equals(getSessionid()))) {
                clearSession();
                return false;
            }
            
            // EVERYTHING SEEMS TO BE IN ORDER
            return true;
        }
        return false;
    }
}
