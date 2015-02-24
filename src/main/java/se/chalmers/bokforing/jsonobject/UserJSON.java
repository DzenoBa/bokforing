
package se.chalmers.bokforing.jsonobject;

import java.io.Serializable;

/**
 *
 * @author Dženan
 */
public class UserJSON implements Serializable{
    
    private static final long serialVersionUID = 1L;
        
    private String email;
    private String passwd;
    private String sessionid;
    private String level;

    public UserJSON() {
        ;
    }
    public UserJSON(String email, String passwd) {
        this.email = email;
        this.passwd = passwd;
        this.sessionid = null;
        this.level = null;
    }
    
    public UserJSON(String email, String sessionid, String level) {
        this.email = email;
        this.passwd = null;
        this.sessionid = sessionid;
        this.level = level;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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
