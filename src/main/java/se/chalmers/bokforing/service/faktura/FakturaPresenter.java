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
import se.chalmers.bokforing.model.faktura.Content;
import se.chalmers.bokforing.model.faktura.Content.Product;
import se.chalmers.bokforing.model.faktura.Faktura;
import se.chalmers.bokforing.model.user.UserHandler;

/**
 *
 * @author victor
 */
public class FakturaPresenter {
    private final static boolean DEBUG = true;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
    private final static DecimalFormat df = new DecimalFormat("#.##");

    private Document doc;
    private final Faktura fak;
    
    private final UserHandler to;
    private final UserHandler fr;
    
    private final Content cont;
    public FakturaPresenter(Faktura faktura){
        this.fak = faktura;
        to = new UserHandler(fak.getToUser());
        fr = new UserHandler(fak.getFromUser());
        cont = fak.getContent();
               
    }
    private String summaryContent(){
        StringBuilder sb = new StringBuilder();
        final Double momsPre = fak.getMomsPrecentage();
        sb.append("Belopp utan moms: " + df.format(cont.getTotalPrice()) + "\n");
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
        replacer("kundnr","INGET KUND NUMMER");
        replacer("faktdat",sdf.format(fak.getFakturaDate()));
        replacer("tcnamn",to.getCompanyName());
        replacer("tadr",to.getCompanyAdr());
        replacer("poskod", to.getPostCode());
        
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
        replacer("fcname",fr.getCompanyName());
        replacer("vadr",fr.getCompanyAdr());
        replacer("vpostkod",fr.getPostCode());
        
        replacer("ftel",fr.getPhoneNumber());
        

        
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
