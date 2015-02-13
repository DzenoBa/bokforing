package se.chalmers.bokforing;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControllerTest {

    /**
     * Request mapping for user
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView getUsersView() {
        String stringToSend = "<br><div align='center'>"
                + "<h3>Hello, World!</h3>Bokföring?<br><br>";
        return new ModelAndView("test", "message", stringToSend);
    }

}
