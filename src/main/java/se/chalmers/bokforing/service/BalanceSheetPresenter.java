/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import com.lowagie.text.DocumentException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;

/**
 *
 * @author Isabelle
 */

@Service
public class BalanceSheetPresenter {
    
    private final static String inputFilePath = "xhtml" + File.separator + "balanceSheet.xhtml";
    private final static String outputFilePath = PresenterHelper.pdfPath;
            
    //String outputFile = getClass().getResource("/").toString().substring(6);

    private final static boolean DEBUG = false;
    Map<Account, List<Double>> balanceSheet;


    @Autowired
    private PostService postService;
    
    @Autowired
    private VerificationService verificationService;

    //Generates all assetAccounts
    private String assetAccountsGenerator() {
        StringBuilder sb = new StringBuilder();
        Set<Account> accountSet = balanceSheet.keySet();

        for (Account acc : accountSet) {
            if (acc.getNumber() < 2000) {
                sb.append("<tr>");
                sb.append("<td>").append(acc.getNumber()).append("  ").append(acc.getName()).append("</td>");
                sb.append("<td>").append(balanceSheet.get(acc).get(1)).append("</td>");
                sb.append("<td>").append(balanceSheet.get(acc).get(0)).append("</td>");
                sb.append("</tr>");
            }
        }

        return sb.toString();
    }

    //Generates all debtAccounts
    private String debtAccountsGenerator() {
        StringBuilder sb = new StringBuilder();
        Set<Account> accountSet = balanceSheet.keySet();

        for (Account acc : accountSet) {
            if (acc.getNumber() >= 2000) {
                sb.append("<tr>");
                sb.append("<td>").append(acc.getNumber()).append("  ").append(acc.getName()).append("</td>");
                sb.append("<td>").append(balanceSheet.get(acc).get(1)).append("</td>");
                sb.append("<td>").append(balanceSheet.get(acc).get(0)).append("</td>");
                sb.append("</tr>");
            }
        }

        return sb.toString();
    }

    //Generates the result
    private String resultGenerator() {
        StringBuilder sb = new StringBuilder();
        Set<Account> accountSet = balanceSheet.keySet();
        double startingBalance = 0.0;
        double totalBalance = 0.0;
        for (Account acc : accountSet) {

            startingBalance += balanceSheet.get(acc).get(1);
            totalBalance += balanceSheet.get(acc).get(0);
        }
        totalBalance += startingBalance;
      
        sb.append("<td>").append(startingBalance).append("</td>");
        sb.append("<td colspan=2>").append(totalBalance).append("</td>");
      

        return sb.toString();
    }
    
    public String print(UserAccount user, Date startDate,
            Date endDate, Pageable pageable) throws IOException, DocumentException {
         balanceSheet = postService.getBalanceSheet(user, startDate, endDate, pageable);
         File input = new File(inputFilePath);

        //File input = new File("/xhtml/balanceSheet.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        PresenterHelper presentHelper = new PresenterHelper(doc);

        //SPECIFICATION
        presentHelper.replacer("fornamn", user.getUserInfo().getName());
        presentHelper.replacer("utskrd", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        presentHelper.replacer("raken", new SimpleDateFormat("yyyy").format(startDate));
        presentHelper.replacer("valdper", new SimpleDateFormat("dd/MM/yyyy").format(startDate) + " - "
                + new SimpleDateFormat("dd/MM/yyyy").format(endDate));

        //VERIFICATION NUMBER
        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "verificationNumber");
        Page<Verification> pageVer = verificationService.findAllVerifications(user, terms);
        if(pageVer.getContent().size() > 0)
            presentHelper.replacer("verinr", pageVer.getContent().get(0).getVerificationNumber().toString());
        else
            presentHelper.replacer("verinr", "");

        //ASSETS SECTION
        presentHelper.replacerHTML("tillgkonto", assetAccountsGenerator());

        //DEBT SECTION
        presentHelper.replacerHTML("skuldkonto", debtAccountsGenerator());

        //FINAL SECTION
        presentHelper.replacerHTML("ingresultat", resultGenerator());

        if (DEBUG) {
            System.out.println(doc.outerHtml());
        }

        String outputFile = outputFilePath + File.separator + "balanceSheet.pdf";
        
        //String outputFile = "/pdf/balansrapport.pdf";
        try (OutputStream os = new FileOutputStream(outputFile)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(doc.outerHtml());
            renderer.layout();
            renderer.createPDF(os);
        }
        return outputFile;
    }

}
