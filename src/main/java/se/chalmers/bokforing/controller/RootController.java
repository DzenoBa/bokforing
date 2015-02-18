/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
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
