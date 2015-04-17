
package se.chalmers.bokforing.controller;

import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.jsonobject.AccountJSON;
import se.chalmers.bokforing.model.AccountType;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.persistence.user.UserService;
import se.chalmers.bokforing.service.PostService;
import se.chalmers.bokforing.session.AuthSession;

/**
 *
 * @author Dženan
 */
@Controller
public class StatisticsController {
    
    @Autowired 
    private AuthSession authSession;
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;
    
    /*
     * GET BALANCE LIST BY ACCOUNT
     */
    @RequestMapping(value = "/statistics/balancelist", method = RequestMethod.POST)
    public @ResponseBody Map<Date, Double> getBalanceListByAccount(@RequestBody final AccountJSON account) {
        
        // CHECK SESSION
        if(!(authSession.sessionCheck())) {
            return null;
        }
        UserHandler uh = userService.getUser(authSession.getEmail());
        
        Date end = new Date();
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date start = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
        
        AccountType accType = AccountType.ASSETS;
        if(account.getNumber() == 2) 
            accType = AccountType.FUNDS_AND_DEBT;
        // MORE?
        
        Map<Date, Double> map = postService.getBalanceForAccountAtDate(uh.getUA(), accType, start, end);
        
        return map;
    }
}
