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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Invoice;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.UserInfo;

/**
 *
 * @author victor
 */
public class InvoicePresenter {

    private final static boolean DEBUG = false;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
    private final static DecimalFormat df = new DecimalFormat("#.##");
    private final static String inputFilePath = "xhtml" + File.separator + "faktura.xhtml";
    private final static String outputFilePath = PresenterHelper.pdfPath;
            
    private final Invoice fak;

    private final Customer buyer;
    private final UserInfo seller;

    public InvoicePresenter(Invoice faktura) {
        this.fak = faktura;
        buyer = fak.getBuyer();
        seller = fak.getSeller();
    }

    private double getTotalPrice(HashMap<Product, Integer> cont) {
        double cost = 0;
        for (Map.Entry<Product, Integer> entry : cont.entrySet()) {
            cost += entry.getKey().getPrice() * entry.getValue();
        }
        return cost;
    }

    private String summaryContent(HashMap<Product, Integer> cont) {
        StringBuilder sb = new StringBuilder();
        double cost = getTotalPrice(cont);
        final Double momsPre = fak.getMoms();
        sb.append("Belopp utan moms: " + df.format(cost) + " kr\n");
        sb.append("Moms kr " + (momsPre * 100) + "% " + df.format(cost * momsPre) + " kr\n");
        sb.append("Belopp att betala: " + df.format(cost + (cost * momsPre)) + " kr");
        return sb.toString();
    }

    private String contentListGenerator(HashMap<Product, Integer> cont) {
        StringBuilder sb = new StringBuilder();
        //HeaderRow
        sb.append("<table class=\"contentTable\">");
        sb.append("<tr>");
        sb.append("<th>Produkt</th>");
        sb.append("<th>Antal</th>");
        sb.append("<th>Kostnad</th>");
        sb.append("</tr>");
        //Start With Content
        for (Map.Entry<Product, Integer> entry : cont.entrySet()) {
            sb.append("<tr>");
            sb.append("<td>").append(entry.getKey().getName()).append("</td>");
            sb.append("<td>").append(entry.getValue()).append("</td>");
            sb.append("<td>").append(df.format((Double) (entry.getKey().getPrice() * entry.getValue()))).append(" kr</td>");
            sb.append("</tr>");
        }

        //End Table
        sb.append("</table>");
        return sb.toString();
    }

    private String invoiceToHTML(HashMap<Product, Integer> cont) throws IOException {
        File input = new File(inputFilePath);
        Document doc = Jsoup.parse(input, "UTF-8");

        PresenterHelper ph = new PresenterHelper(doc);

        if (fak.isValid()) {
            ph.replacer("head", "Faktura");
        } else {
            ph.replacerHTML("head", "<a style=\"color: red;\">Ogiltig Faktura</a>");
        }

        //TOP
        ph.replacer("faktnr", fak.getFakturaId().toString());
        ph.replacer("kundnr", buyer.getCustomerNumber().toString());
        ph.replacer("faktdat", sdf.format(fak.getFakturaDate()));
        ph.replacer("tcnamn", buyer.getAddress().getCompanyName());
        ph.replacer("tadr", buyer.getAddress().getStreetNameAndNumber());
        ph.replacer("poskod", buyer.getAddress().getPostalCode());

        //MID/TOP
        ph.replacer("ernamn", buyer.getName());
        ph.replacer("ordnr", "NUMMER SAKNAS");

        ph.replacer("vnamn", seller.getName());
        ph.replacer("betvil", "30 dagars betalnings tid");
        ph.replacer("exdat", sdf.format(fak.getExpireDate()));
        ph.replacer("intrest", "50 kr betalnings ränta");

        //MID
        ph.replacerHTML("content", contentListGenerator(cont));
        ph.replacer("totalcost", summaryContent(cont));

        //BOT
        ph.replacer("fcname", seller.getAddress().getCompanyName());
        ph.replacer("vadr", seller.getAddress().getStreetNameAndNumber());
        ph.replacer("vpostkod", seller.getAddress().getPostalCode());

        ph.replacer("ftel", seller.getPhoneNumber());

        StringBuilder sb = new StringBuilder();
        sb.append("<ul><li>");
        sb.append(fak.getMomsNumber());
        sb.append("</li><li>");
        if (fak.isFtax()) {
            sb.append("Godkänd för F-skatt");
        } else {
            sb.append("<b>Ej</b> godkänd för F-skatt");
        }
        sb.append("</li></ul>");

        ph.replacerHTML("momsinfo", sb.toString());

        ph.replacer("bankgiro", seller.getBankgiro());

        if (DEBUG) {
            System.out.println(doc.outerHtml());
        }
        return doc.outerHtml();
    }

    private List<HashMap<Product, Integer>> generateInvoice() {
        final List<HashMap<Product, Integer>> tcont = new ArrayList<>();
        final Iterator<Entry<Product, Integer>> ite = fak.products().entrySet().iterator();
        int fakNum = -1;
        int i = 0;
        while (ite.hasNext()) {
            final Entry<Product, Integer> ent = ite.next();
            if (i % 15 == 0) {
                tcont.add(new HashMap<Product, Integer>());
                Calendar cal = Calendar.getInstance();
                cal.setTime(fak.getFakturaDate());
                cal.add(Calendar.DAY_OF_MONTH, 30);
                fak.setExpireDate(cal.getTime());
                fakNum++;
                //Do the next page
            }
            tcont.get(fakNum).put(ent.getKey(), ent.getValue());
            i++;
        }

        return tcont;
    }

    public String print() throws IOException, DocumentException {

        final List<HashMap<Product, Integer>> list = generateInvoice();
        final String outputFileName = "Faktura " + fak.getFakturaId().toString() + " "+ fak.getSeller().getName() + ", " + sdf.format(fak.getFakturaDate()) + ".pdf";
        final String outputFile = outputFilePath + File.separator + outputFileName;
        try (OutputStream os = new FileOutputStream(outputFile)) {

            final Iterator<HashMap<Product, Integer>> ite = list.iterator();

            final ITextRenderer renderer = new ITextRenderer();

            String html = invoiceToHTML(ite.next());
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(os, false);

            while (ite.hasNext()) {
                html = invoiceToHTML(ite.next());
                renderer.setDocumentFromString(html);
                renderer.layout();
                renderer.writeNextDocument();
            }

            renderer.finishPDF();
        }
        return outputFile;
    }

}
