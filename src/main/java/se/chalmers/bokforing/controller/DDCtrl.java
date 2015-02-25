
package se.chalmers.bokforing.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.persistence.Group;
import se.chalmers.bokforing.persistence.UserDb;
import se.chalmers.bokforing.persistence.UserEnt;

/**
 * Default Data Controller (DDCtrl)
 * 
 * @author Dženan
 */
@Controller
public class DDCtrl {

    @Autowired
    private UserDb userDb;
    
    /*
     * SET
     */
    @RequestMapping(value = "/dd/set", method = RequestMethod.GET)
    public @ResponseBody FormJSON set() {
        System.out.println("* PING DD/get");

        FormJSON form = new FormJSON();

        UserEnt userEnt = userDb.getUser("dzeno@bazdar.ba");
        
        if(userEnt == null) {
            // CREATE A NEW USER
            UserEnt u = new UserEnt();
            u.setEmail("dzeno@bazdar.ba");
            u.setPass("passwd");
            u.setGroup(Group.Admin);
            userDb.storeUser(u);
            
            return form;
        }
        // USER ALREADY EXIST
        else {
            form.addError("create", "E-post adressen 'dzeno@bazdar.ba' finns redan i databasen!");
            
            return form;
        }
    }
}
