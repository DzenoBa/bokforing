/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.service.InitializationUtil;

/**
 *
 * @author Dženan
 */
@Controller
public class DefaultDataController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private InitializationUtil initUtil;
    
    @RequestMapping(value = "/defaultdata/accounts", method = RequestMethod.GET)
    public @ResponseBody FormJSON initAccounts() {
        
        System.out.println("* PING defaultdata/accounts");
        FormJSON form = new FormJSON();
        
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, false, "name");
        if(accountService.findAllAccounts(terms).getNumberOfElements() > 0) {
            form.addError("general", "Denna funktion kan inte användas eftersom data redan finns i tabellen");
            return form;
        } else {
            if(!initUtil.insertDefaultAccounts()) {
                form.addError("general", "Något fick fel");
                return form;
            }
        }
        
        return form;
    }
}
