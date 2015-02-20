package se.chalmers.bokforing;

import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import se.chalmers.bokforing.rep.UserEnt;
import se.chalmers.bokforing.rep.UserRepository;

@Controller
public class ControllerTest {

    /**
     * Request mapping for user
     * @return 
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView getUsersView() {
        String stringToSend = "<br><div align='center'>"
                + "<h3>Hello, World!</h3>Bokföring?<br><br>";
        userRep.save(new UserEnt());
        return new ModelAndView("test", "message", userRep.findById(0).toString());
    }
    
    @Autowired
    UserRepository userRep;
    
    //@Autowired
    //EntityManager eM;

}
