/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.faktura;

import com.lowagie.text.DocumentException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xhtmlrenderer.pdf.ITextRenderer;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.faktura.Content;
import se.chalmers.bokforing.model.faktura.Content.Product;
import se.chalmers.bokforing.model.faktura.Faktura;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
public class FakturaPresenter {
    private final static boolean DEBUG = false;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
    private final static DecimalFormat df = new DecimalFormat("#.##");

    private Document doc;
    private final Faktura fak;
    
    private final Customer to;
    private final UserInfo fr;
    
    private final Content cont;
    public FakturaPresenter(Faktura faktura){
        this.fak = faktura;
        to = fak.getToUser();
        fr = fak.getFromUser();
        //cont = fak.getContent();
        Content prods = new Content();
        for(int i = 0; i < 15; i++)
            prods.addProduct("Prod " + i, 100.0, i);
        cont = prods;
               
    }
    private String summaryContent(){
        StringBuilder sb = new StringBuilder();
        final Double momsPre = fak.getMomsPrecentage();
        sb.append("Belopp utan moms: " + df.format(cont.getTotalPrice()) + " kr\n");
        sb.append("Moms kr " + (momsPre * 100) + "% " + df.format(cont.getTotalPrice() * momsPre) + " kr\n");
        sb.append("Belopp att betala: " + df.format(cont.getTotalPrice() + (cont.getTotalPrice() * momsPre)) + " kr");
        return sb.toString();
    }
    private void replacer(String tag, String replaceWith){
        Element replace = doc.select("a[id="+tag+"]").first();
        replace.text(replaceWith);
    }
    private String contentListGenerator(){
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
        while(it.hasNext()){
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
    public void print() throws IOException, DocumentException {        
        File input = new File("xhtml/faktura.xhtml");
        doc = Jsoup.parse(input, "UTF-8");
                
        //TOP
        replacer("faktnr",fak.getFakturaId().toString());
        replacer("kundnr",to.getCustomerNumber().toString());
        replacer("faktdat",sdf.format(fak.getFakturaDate()));
        replacer("tcnamn",to.getAddress().getCompanyName());
        replacer("tadr",to.getAddress().getStreetNameAndNumber());
        replacer("poskod", to.getAddress().getPostalCode());
        
        //MID/TOP
        replacer("ernamn",to.getName());
        replacer("ordnr","INGET ORDER NUMMER");
        
        replacer("vnamn", fr.getName());
        replacer("betvil","INGA VILKOR");
        replacer("exdat",sdf.format(fak.getExpireDate()));
        replacer("intrest", "INGEN DRÖJSMÅLSRÄNTA");
        
        //MID
        Element replace = doc.select("a[id=content]").first();
        replace.html(contentListGenerator());
        replacer("totalcost",summaryContent());
        
        //BOT
        replacer("fcname",fr.getAddress().getCompanyName());
        replacer("vadr",fr.getAddress().getStreetNameAndNumber());
        replacer("vpostkod",fr.getPostCode());
        
        replacer("ftel",fr.getPhoneNumber());
        
        StringBuilder sb = new StringBuilder();
        sb.append("<ul><li>");
        sb.append(fak.getMomsRegistredNumber());
        sb.append("</li><li>");
        if(fak.getFskatt()){
            sb.append("Godkänd för F-skatt");
        } else{
            sb.append("<b>Ej</b> godkänd för F-skatt");
        }
        sb.append("</li></ul>");
        
        replace = doc.select("a[id=momsinfo]").first();
        replace.html(sb.toString());
        
        replacer("bankgiro",fr.getBankgiro());
        

        
        if(DEBUG)
            System.out.println(doc.outerHtml());
        
        String outputFile = "pdf/fak" + fak.getFakturaId().toString() + ".pdf";
        try (OutputStream os = new FileOutputStream(outputFile)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(doc.outerHtml());
            renderer.layout();
            renderer.createPDF(os);
        }
    }
    
}
