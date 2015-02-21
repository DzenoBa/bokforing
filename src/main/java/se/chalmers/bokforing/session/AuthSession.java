
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
    private String username;
    private String sessionid;
    private String level;
    
    public boolean getStatus() {
        return status;
    }
    public void setSatus(boolean status) {
        this.status = status;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
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
    
    public void clearSession() {
        status = false;
        username = null;
        sessionid = null;
        level = null;
    }
}
