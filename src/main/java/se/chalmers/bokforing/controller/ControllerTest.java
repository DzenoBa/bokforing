package se.chalmers.bokforing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import se.chalmers.bokforing.persistence.UserDb;
import se.chalmers.bokforing.persistence.UserEnt;
import se.chalmers.bokforing.persistence.UserRepository;

@Controller
public class ControllerTest {
     @Autowired
    private UserRepository userRep;

    /**
     * Request mapping for user
     * @return 
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView getUsersView() {
        String stringToSend = "<br><div align='center'>"
                + "<h3>Hello, World!</h3>Bokföring?<br><br>";
        UserEnt user = new UserEnt();
        user.setName("Victor");
        user.setEmail("whoop");
        user.setPass("whoop");
        UserDb.storeUser(userRep,user);
        return new ModelAndView("test", "message", UserDb.getUsersByName(userRep,"Victor").toString());
    }
    
    @RequestMapping("/test2")
    public String getNewPage() {
        return "test3";
    }

}
