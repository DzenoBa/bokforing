/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.presenter;

import com.lowagie.text.DocumentException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhtmlrenderer.pdf.ITextRenderer;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.service.PostService;

/**
 *
 * @author Isabelle
 */
@Service
@Transactional
public class IncomeStatementPresenter {

    private final static boolean DEBUG = false;
    Map<Account, Double> incomeStatement;

    @Autowired
    private PostService postService;

    //Generates all assetAccounts
    private String revenueAccountsGenerator() {
        StringBuilder sb = new StringBuilder();
        Set<Account> accountSet = incomeStatement.keySet();

        for (Account acc : accountSet) {
            if (acc.getNumber() / 1000 == 3) {
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
            if (acc.getNumber() / 1000 >= 4) {
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
        incomeStatement = postService.getIncomeStatement(user, startDate,
                endDate, pageable);
        File input = new File(getClass().getResource("/xhtml/incomeStatement.xhtml").toString().substring(6));
        Document doc = Jsoup.parse(input, "UTF-8");
        PresenterHelper ph = new PresenterHelper(doc);

        //SPECIFICATION
        //don't know how to find company name
        ph.replacer("fornamn", user.getUserInfo().getName());
        ph.replacer("utskrd", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        ph.replacer("raken", new SimpleDateFormat("yyyy").format(startDate));
        ph.replacer("valdper", new SimpleDateFormat("dd/MM/yyyy").format(startDate) + " - "
                + new SimpleDateFormat("dd/MM/yyyy").format(endDate));

        //VERIFICATION NUMBER
        ph.replacer("verinr", user.getVerifications().get(user.getVerifications().size() - 1).getVerificationNumber().toString());

        //REVENUE SECTION
        ph.replacerHTML("intaktkonto", revenueAccountsGenerator());

        //COST SECTION
        ph.replacerHTML("kostnadkonto", costAccountsGenerator());

        //RESULT SECTION
        ph.replacerHTML("resultat", resultGenerator());

        if (DEBUG) {
            System.out.println(doc.outerHtml());
        }
        String outputFile = getClass().getResource("/").toString().substring(6);
        outputFile = outputFile + "incomeStatement.pdf";
        //String outputFile = "/pdf/balansrapport.pdf";
        try (OutputStream os = new FileOutputStream(outputFile)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(doc.outerHtml());
            renderer.layout();
            renderer.createPDF(os);
        }
    }
}
