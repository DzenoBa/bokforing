
package se.chalmers.bokforing.jsonobject;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Dženan
 */
public class ReportJSON implements Serializable {
    
    private Date start;
    
    public Date getStart() {
        return start;
    }
    
    public void setStart(Date start) {
        this.start = start;
    }
}
