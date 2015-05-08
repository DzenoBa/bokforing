/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author victor
 */
class PresenterHelper {
    Document doc;
    PresenterHelper(Document doc){
        this.doc = doc;
    }
    
    private static String nullReplace(String str){
        if(str == null)
            str = "";
        return str;
    }
    
    void replacer(String tag, String replaceWith) {
        replaceWith = nullReplace(replaceWith);
        Element replace = doc.select("a[id=" + tag + "]").first();
        replace.text(replaceWith);
    }

    void replacerHTML(String tag, String replaceWith) {
        replaceWith = nullReplace(replaceWith);
        Element replace = doc.select("a[id=" + tag + "]").first();
        replace.html(replaceWith);
    }
}
