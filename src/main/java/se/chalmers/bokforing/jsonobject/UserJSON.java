
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
    private String level;

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
    
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
}
