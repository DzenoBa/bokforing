/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.model.User;

/**
 *
 * @author Jakob
 */
@Controller
public class ResponseBodyJSONTest {
    
    @RequestMapping("/users/{userName}")
    public @ResponseBody User getUser(@PathVariable String userName) {
        User user = new User();
        user.setName(userName);
        user.setPassword("password");
        
        return user;
    }
    
    @RequestMapping("/users")
    public @ResponseBody User getUserById(
            @RequestParam(value = "userName", required = false, defaultValue = "Apa") String userName,
            @RequestParam(value = "userId", required = false, defaultValue = "123") String userId
    ) {
        User user = new User();
        user.setName(userName);
        user.setPassword("password");
        user.setId(userId);
        
        return user;
    }
    
}
