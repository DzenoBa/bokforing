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
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xhtmlrenderer.pdf.ITextRenderer;
import se.chalmers.bokforing.model.faktura.Content;
import se.chalmers.bokforing.model.faktura.Faktura;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
public class FakturaPresenter {
    private Document doc;
    
    private final Faktura fak;
    
    //The big list of privates
    //From
    private final String fName;
    private final String fComp;
    private final String mNumber;
    private final Boolean fTax;
    
    //To
    private final String tName;
    private final String tComp;
    
    private final Date fakDate;
    private final Date expireDate;
    
    private final Long fakId;
    
    private final Content cont;
    
    private final Double totalCost;
    
    private final Double moms;
    
    private final Double momsPrecentage;
    
    public FakturaPresenter(){
        fak = null;
        fName = "Mister Reciver";
        fComp = "undef";
        mNumber = "undef";
        fTax = false;
        
        tName = "undef";
        tComp = "undef";
        
        fakDate = new Date();
        expireDate = new Date();
        
        fakId = (long) 0;
        
        cont = null;
        totalCost = null;
        
        moms = 0.0;
        momsPrecentage = 0.0;
    }
    
    public FakturaPresenter(Faktura faktura){
        this.fak = faktura;
        UserInfo to = fak.getToUser();
        UserInfo fr = fak.getFromUser();
        
        fName = fr.getName();
        fComp = fr.getCompanyName();
        mNumber = fak.getMomsRegistredNumber();
        fTax = fak.getFskatt();
        
        tName = to.getName();
        tComp = to.getCompanyName();
        
        fakDate = fak.getFakturaDatum();
        expireDate = fak.getExpireDate();
        
        fakId = fak.getFakturaId();
        
        cont = fak.getContent();
        totalCost = cont.getTotalPrice();
        
        moms = fak.getMomsCost();
        momsPrecentage = fak.getMomsPrecentage();
        
    }

    private void replacer(String tag, String replaceWith){
        Element replace = doc.select("a[id="+tag+"]").first();
        replace.text(replaceWith);
    }
    
    public void print() throws IOException, DocumentException {        
        File input = new File("xhtml/faktura.xhtml");
        doc = Jsoup.parse(input, "UTF-8");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        
        //TOP
        replacer("faktnr",fakId.toString());
        replacer("kundnr","INGET KUND NUMMER");
        replacer("faktdat",sdf.format(fakDate));
        replacer("tcnamn",tComp);
        replacer("tadr","INTE KUNDADRESS");
        replacer("poskod", "INTE POSTKOD");
        
        //MID/TOP
        replacer("ernamn",tName);
        replacer("ordnr","INGET ORDER NUMMER");
        
        replacer("vnamn", fName);
        replacer("betvil","INGA VILKOR");
        replacer("exdat",sdf.format(expireDate));
        replacer("intrest", "INGEN DRÖJSMÅLSRÄNTA");
        
        //MID
        
        
        //BOT
        

        
       
        System.out.println(doc.outerHtml());
        String outputFile = "pdf/fak" + fakId + ".pdf";
        try (OutputStream os = new FileOutputStream(outputFile)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(doc.outerHtml());
            renderer.layout();
            renderer.createPDF(os);
        }
    }
    
}
