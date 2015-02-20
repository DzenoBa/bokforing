package se.chalmers.bokforing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import se.chalmers.bokforing.rep.UserEnt;
import se.chalmers.bokforing.rep.UserRepository;

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
        user.setId(0);
        user.setName("Johan");
        
        UserEnt user2 = new UserEnt();
        user2.setId(1);
        user2.setName("Apa");
        user2.setPass("password");
        userRep.save(user);
        return new ModelAndView("test", "message", userRep.findByName("Johan").toString());
    }
}