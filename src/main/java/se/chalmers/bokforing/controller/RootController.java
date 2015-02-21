package se.chalmers.bokforing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This controller exists to map our index page.
 * 
 * @author Jakob
 */
@Controller
@RequestMapping("/")
public class RootController {
    
    @RequestMapping(method = RequestMethod.GET)
    public String getIndex() {
        return "index";
    }
}
