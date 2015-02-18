
package se.chalmers.bokforing.jsonobject;

import java.io.Serializable;

/**
 *
 * @author Dženan
 */
public class UserJSON implements Serializable{
    
    private static final long serialVersionUID = 1L;
        
    private String username;
    private String passwd;
    private String sessionid;
    private String level;

    public UserJSON() {
        ;
    }
    public UserJSON(String username, String passwd) {
        this.username = username;
        this.passwd = passwd;
        this.sessionid = null;
        this.level = null;
    }
    
    public UserJSON(String username, String sessionid, String level) {
        this.username = username;
        this.passwd = null;
        this.sessionid = sessionid;
        this.level = level;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    
    public String getSessionid() {
        return sessionid;
    }
    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
}
