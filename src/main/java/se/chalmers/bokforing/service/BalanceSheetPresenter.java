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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Element;
import org.dom4j.Document;
import org.jsoup.Jsoup;
import org.springframework.data.domain.Pageable;
import org.xhtmlrenderer.pdf.ITextRenderer;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.user.UserAccount;

/**
 *
 * @author Isabelle
 */
public class BalanceSheetPresenter {

    private final static boolean DEBUG = true;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
    private final static DecimalFormat df = new DecimalFormat("#.##");
    Map<Account, List<Double>> balanceSheet;

    private Document doc;

    // Maybe not correct
    private PostServiceImpl postService;

//    private final UserHandler to;
//    private final UserHandler fr;
//    private final Content cont;
    public BalanceSheetPresenter(UserAccount user, Date startDate,
            Date endDate, Pageable pageable) {
        balanceSheet = postService.getBalanceSheet(user, startDate,
                endDate, pageable);
//        this.fak = faktura;
//        to = new UserHandler(fak.getToUser());
//        fr = new UserHandler(fak.getFromUser());
//        cont = fak.getContent();

    }

    private String summaryContent() {
        StringBuilder sb = new StringBuilder();
        final Double momsPre = fak.getMomsPrecentage();
        sb.append("Belopp utan moms: " + df.format(cont.getTotalPrice()) + "\n");
        sb.append("Moms kr " + (momsPre * 100) + "% " + df.format(cont.getTotalPrice() * momsPre) + " kr\n");
        sb.append("Belopp att betala: " + df.format(cont.getTotalPrice() + (cont.getTotalPrice() * momsPre)) + " kr");
        return sb.toString();
    }

    private void replacer(String tag, String replaceWith) {
        Element replace = doc.select("a[id=" + tag + "]").first();
        replace.text(replaceWith);
    }

    private String assetsGenerator() {
        StringBuilder sb = new StringBuilder();
        //HeaderRow
        sb.append("<table class=\"contentTable\">");
        sb.append("<tr>");
        sb.append("<th>Produkt</th>");
        sb.append("<th>Antal</th>");
        sb.append("<th>Kostnad</th>");
        sb.append("</tr>");
        //Start With Content
        Iterator<Product> it = cont.getIterator();
        while (it.hasNext()) {
            Product p = it.next();
            sb.append("<tr>");
            sb.append("<td>").append(p.getName()).append("</td>");
            sb.append("<td>").append(p.getUnits()).append("</td>");
            sb.append("<td>").append(df.format((Double) (p.getPrice() * p.getUnits()))).append(" kr</td>");
            sb.append("</tr>");
        }

        //End Table
        sb.append("</table>");
        return sb.toString();
    }
     private String debtGenerator() {
        StringBuilder sb = new StringBuilder();
        //HeaderRow
        sb.append("<table class=\"contentTable\">");
        sb.append("<tr>");
        sb.append("<th>Produkt</th>");
        sb.append("<th>Antal</th>");
        sb.append("<th>Kostnad</th>");
        sb.append("</tr>");
        //Start With Content
        Iterator<Product> it = cont.getIterator();
        while (it.hasNext()) {
            Product p = it.next();
            sb.append("<tr>");
            sb.append("<td>").append(p.getName()).append("</td>");
            sb.append("<td>").append(p.getUnits()).append("</td>");
            sb.append("<td>").append(df.format((Double) (p.getPrice() * p.getUnits()))).append(" kr</td>");
            sb.append("</tr>");
        }

        //End Table
        sb.append("</table>");
        return sb.toString();
    }

    public void print(UserAccount user, Date startDate,
            Date endDate, Pageable pageable) throws IOException, DocumentException {
        File input = new File("xhtml/faktura.xhtml");
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
        Element replace = doc.select("a[id=assets]").first();
        replace.html(assetsGenerator());

        //DEBT SECTION
        Element replace = doc.select("a[id=assets]").first();
        replace.html(debtGenerator());

        //FINAL SECTION
        replacer("totalcost", summaryContent());

        if (DEBUG) {
            System.out.println(doc.outerHtml());
        }

        String outputFile = "pdf/fak" + fak.getFakturaId().toString() + ".pdf";
        try (OutputStream os = new FileOutputStream(outputFile)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(doc.outerHtml());
            renderer.layout();
            renderer.createPDF(os);
        }
    }

}
