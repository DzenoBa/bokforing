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
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhtmlrenderer.pdf.ITextRenderer;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.UserAccount;

/**
 *
 * @author Isabelle
 */

@Service
@Transactional
public class BalanceSheetPresenter {

    private final static boolean DEBUG = true;
    Map<Account, List<Double>> balanceSheet;

    private Document doc;

    @Autowired
    private PostService postService;

    private void replacer(String tag, String replaceWith) {
        Element replace = doc.select("a[id=" + tag + "]").first();
        replace.text(replaceWith);
    }

    //Generates all assetAccounts
    private String assetAccountsGenerator() {
        StringBuilder sb = new StringBuilder();
        Set<Account> accountSet = balanceSheet.keySet();

        for (Account acc : accountSet) {
            if (acc.getNumber() < 2000) {
                sb.append("<tr>");
                sb.append("<td>").append(acc.getNumber()).append(acc.getName()).append("</td>");
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
                sb.append("<td>").append(acc.getNumber()).append(acc.getName()).append("</td>");
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
        sb.append("<tr>");
        sb.append("<td>").append(startingBalance).append("</td>");
        sb.append("<td colspan=2>").append(totalBalance).append("</td>");
        sb.append("</tr>");

        return sb.toString();
    }

    public void print(UserAccount user, Date startDate,
            Date endDate, Pageable pageable) throws IOException, DocumentException {
         balanceSheet = postService.getBalanceSheet(user, startDate,
                endDate, pageable);
        File input = new File("balanceSheet.html");
        doc = Jsoup.parse(input, "UTF-8");

        //SPECIFICATION
        //don't know how to find company name
        //replacer("fornamn", fak.getFakturaId().toString());
        replacer("utskrd", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        replacer("raken", new SimpleDateFormat("yyyy").format(startDate));
        replacer("valdper", new SimpleDateFormat("dd/MM/yyyy").format(startDate) + " - "
                + new SimpleDateFormat("dd/MM/yyyy").format(endDate));

        //VERIFICATION NUMBER
        replacer("verinr", user.getVerifications().get(user.getVerifications().size() - 1).getVerificationNumber().toString());

        //ASSETS SECTION
        replacer("tillgkonto", assetAccountsGenerator());

        //DEBT SECTION
        replacer("skuldkonto", debtAccountsGenerator());

        // SECTION
        replacer("ingresultat", resultGenerator());

        if (DEBUG) {
            System.out.println(doc.outerHtml());
        }

        String outputFile = "pdf/balansrapport.pdf";
        try (OutputStream os = new FileOutputStream(outputFile)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(doc.outerHtml());
            renderer.layout();
            renderer.createPDF(os);
        }
    }

}
