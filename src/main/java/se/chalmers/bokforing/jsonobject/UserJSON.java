
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
    private String newpasswd;
    private String newpasswd2;
    private String level;
    private String accesskey;

    public UserJSON() {
        ;
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
    
    public String getNewpasswd() {
        return newpasswd;
    }
    public void setNewpasswd(String newpasswd) {
        this.newpasswd = newpasswd;
    }
    
    public String getNewpasswd2() {
        return newpasswd2;
    }
    public void setNewpasswd2(String newpasswd) {
        this.newpasswd2 = newpasswd;
    }
    
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    
    public String getAccesskey() {
        return accesskey;
    }
    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }
}
