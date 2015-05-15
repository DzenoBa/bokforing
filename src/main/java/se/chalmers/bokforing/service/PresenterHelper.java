/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.io.File;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author victor
 */
class PresenterHelper {

    public final static String pdfPath = "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "public" + File.separator + "pdf";

    Document doc;

    PresenterHelper(Document doc) {
        this.doc = doc;
        File file = new File(pdfPath);
        if (!file.exists() && !file.mkdirs()) {
            // Can not make the directory
        } else {
            // Directories exists or was created
        }
    }

    private static String nullReplace(String str) {
        if (str == null) {
            str = "";
        }
        return str;
    }

    void replacer(String tag, String replaceWith) {
        replaceWith = nullReplace(replaceWith);
        Element replace = doc.select("[id=" + tag + "]").first();
        replace.text(replaceWith);
    }

    void replacerHTML(String tag, String replaceWith) {
        replaceWith = nullReplace(replaceWith);
        Element replace = doc.select("[id=" + tag + "]").first();
        replace.html(replaceWith);
    }
}
