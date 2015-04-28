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
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.Element;
import org.dom4j.Document;
import org.jsoup.Jsoup;
import org.springframework.data.domain.Pageable;
import org.xhtmlrenderer.pdf.ITextRenderer;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.user.UserAccount;

/**
 *
 * @author Isabelle
 */
public class IncomeStatementPresenter {

    private final static boolean DEBUG = true;
    Map<Account, Double> incomeStatement;

    private Document doc;

    // Maybe not correct
    private PostServiceImpl postService;

    public IncomeStatementPresenter(UserAccount user, Date startDate,
            Date endDate, Pageable pageable) {
        incomeStatement = postService.getIncomeStatement(user, startDate,
                endDate, pageable);

    }

    private void replacer(String tag, String replaceWith) {
        Element replace = doc.select("a[id=" + tag + "]").first();
        replace.text(replaceWith);
    }

    //Generates all assetAccounts
    private String revenueAccountsGenerator() {
        StringBuilder sb = new StringBuilder();
        Set<Account> accountSet = incomeStatement.keySet();

        for (Account acc : accountSet) {
            if (acc.getNumber() < 2000) {
                sb.append("<tr>");
                sb.append("<td>").append(acc.getNumber()).append(acc.getName()).append("</td>");
                sb.append("<td>").append(incomeStatement.get(acc)).append("</td>");
                sb.append("</tr>");
            }
        }

        return sb.toString();
    }

    //Generates all debtAccounts
    private String costAccountsGenerator() {
        StringBuilder sb = new StringBuilder();
        Set<Account> accountSet = incomeStatement.keySet();

        for (Account acc : accountSet) {
            if (acc.getNumber() >= 2000) {
                sb.append("<tr>");
                sb.append("<td>").append(acc.getNumber()).append(acc.getName()).append("</td>");
                sb.append("<td>").append(incomeStatement.get(acc)).append("</td>");
                sb.append("</tr>");
            }
        }

        return sb.toString();
    }

    //Generates the result
    private String resultGenerator() {
        StringBuilder sb = new StringBuilder();
        Set<Account> accountSet = incomeStatement.keySet();
        double totalBalance = 0.0;
        for (Account acc : accountSet) {     
           
            totalBalance += incomeStatement.get(acc);
        }     
                sb.append("<tr>");
                sb.append("<td colspan=2>").append(totalBalance).append("</td>");
                sb.append("</tr>");

        return sb.toString();
    }

    public void print(UserAccount user, Date startDate,
            Date endDate, Pageable pageable) throws IOException, DocumentException {
        File input = new File("incomeStatement.html");
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

        //REVENUE SECTION
        replacer("intaktkonto", revenueAccountsGenerator());

        //COST SECTION
        replacer("kostnadkonto", costAccountsGenerator());

        //RESULT SECTION
        replacer("resultat", resultGenerator());

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
