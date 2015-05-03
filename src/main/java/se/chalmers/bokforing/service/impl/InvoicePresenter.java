/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import com.lowagie.text.DocumentException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xhtmlrenderer.pdf.ITextRenderer;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.orders.Invoice;
import se.chalmers.bokforing.model.orders.OrderEntity;
import se.chalmers.bokforing.model.UserInfo;

/**
 *
 * @author victor
 */
public class InvoicePresenter {

    private final static boolean DEBUG = false;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
    private final static DecimalFormat df = new DecimalFormat("#.##");

    private Document doc;
    private final Invoice fak;

    private final Customer buyer;
    private final UserInfo seller;

    private final HashMap<Product, Integer> cont;

    public InvoicePresenter(Invoice faktura) {
        this.fak = faktura;
        OrderEntity oe = fak.getOrderEntity();
        buyer = oe.getBuyer();
        seller = oe.getSeller();
        cont = fak.Products();

    }

    private double getTotalPrice() {
        double cost = 0;
        for (Map.Entry<Product, Integer> entry : cont.entrySet()) {
            cost += entry.getKey().getPrice() * entry.getValue();
        }
        return cost;
    }

    private String summaryContent() {
        StringBuilder sb = new StringBuilder();
        double cost = getTotalPrice();
        final Double momsPre = fak.getOrderEntity().getMomsPrecentage();
        sb.append("Belopp utan moms: " + df.format(cost) + " kr\n");
        sb.append("Moms kr " + (momsPre * 100) + "% " + df.format(cost * momsPre) + " kr\n");
        sb.append("Belopp att betala: " + df.format(cost + (cost * momsPre)) + " kr");
        return sb.toString();
    }

    private void replacer(String tag, String replaceWith) {
        Element replace = doc.select("a[id=" + tag + "]").first();
        replace.text(replaceWith);
    }
    
    private void replacerHTML(String tag, String replaceWith) {
        Element replace = doc.select("a[id=" + tag + "]").first();
        replace.html(replaceWith);
    }

    private String contentListGenerator() {
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

    public void print() throws IOException, DocumentException {
        File input = new File("xhtml/faktura.xhtml");
        doc = Jsoup.parse(input, "UTF-8");

        if(fak.isValid())
            replacer("head","Faktura");
        else
            replacerHTML("head","<a style=\"color: red;\">Ogiltlig Faktura</a>");
        
        //TOP
        replacer("faktnr", fak.getFakturaId().toString());
        replacer("kundnr", buyer.getCustomerNumber().toString());
        replacer("faktdat", sdf.format(fak.getFakturaDate()));
        replacer("tcnamn", buyer.getAddress().getCompanyName());
        replacer("tadr", buyer.getAddress().getStreetNameAndNumber());
        replacer("poskod", buyer.getAddress().getPostalCode());

        //MID/TOP
        replacer("ernamn", buyer.getName());
        replacer("ordnr", fak.getOrderEntity().getOrderEntityId().toString());

        replacer("vnamn", seller.getName());
        replacer("betvil", "30 dagars betalnings tid");
        replacer("exdat", sdf.format(fak.getExpireDate()));
        replacer("intrest", "50 kr betalnings ränta");

        //MID
        replacerHTML("content",contentListGenerator());
        replacer("totalcost", summaryContent());

        //BOT
        replacer("fcname", seller.getAddress().getCompanyName());
        replacer("vadr", seller.getAddress().getStreetNameAndNumber());
        replacer("vpostkod", seller.getAddress().getPostalCode());

        replacer("ftel", seller.getPhoneNumber());

        StringBuilder sb = new StringBuilder();
        sb.append("<ul><li>");
        sb.append(fak.getOrderEntity().getMomsRegistredNumber());
        sb.append("</li><li>");
        if (fak.getOrderEntity().isFskatt()) {
            sb.append("Godkänd för F-skatt");
        } else {
            sb.append("<b>Ej</b> godkänd för F-skatt");
        }
        sb.append("</li></ul>");

        replacerHTML("momsinfo",sb.toString());

        replacer("bankgiro", seller.getBankgiro());

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
