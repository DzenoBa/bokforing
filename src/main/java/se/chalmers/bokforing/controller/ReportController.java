
package se.chalmers.bokforing.controller;

import com.lowagie.text.DocumentException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.ReportJSON;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.BalanceSheetPresenter;
import se.chalmers.bokforing.service.IncomeStatementPresenter;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.session.AuthSession;

/**
 *
 * @author Dženan
 */
@Controller
public class ReportController {
    
    @Autowired
    private AuthSession authSession;
    
    @Autowired
    private BalanceSheetPresenter balanceSheetPresenter;
    
    @Autowired
    private IncomeStatementPresenter incomeStatementPresenter;
    
    @Autowired
    private UserService userService;
    
    /*
     * GENERATE BALANCE-SHEET-REPORT
     */
    @RequestMapping(value = "/report/balancesheet", method = RequestMethod.POST)
    public @ResponseBody FormJSON getBalanceListByAccount(@RequestBody final ReportJSON report) {
        
        FormJSON form = new FormJSON();
        
        // CHECK SESSION
        if (!(authSession.sessionCheck())) {
            return form;
        }
        UserHandler uh = userService.getUser(authSession.getEmail());
        
        // DATE CHECK
        if(report.getStart() == null) {
            form.addError("gemeral", "Vänligen ange ett datum");
            return form;
        }
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 0);
        Date firstDateOfYear = cal.getTime();
        if(firstDateOfYear.after(report.getStart())) {
            form.addError("general", "Datumet går över årsskiftet");
            return form;
        }
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate");
        
        try {
            balanceSheetPresenter.print(uh.getUA(), report.getStart(), today, terms.getPageRequest());
        } catch (IOException ex) {
            System.out.println(ex);
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund!");
        } catch (DocumentException ex) {
            System.out.println(ex);
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund!");
        }
        return form;
    }
}
