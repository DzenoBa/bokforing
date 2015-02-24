
package se.chalmers.bokforing.session;

import java.io.Serializable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dženan
 */
@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class AuthSession implements Serializable {
    
    private boolean status = false;
    private String email;
    private String sessionid;
    private String level;
    
    public boolean getStatus() {
        return status;
    }
    public void setSatus(boolean status) {
        this.status = status;
    }
    
    public String getEmail() {
        return email;
    }
    public void setUsername(String email) {
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
}
