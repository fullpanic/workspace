package com.test.lwying;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author lwying
 *
 * Aug 21, 2013
 * 
 * Jsoup test case
 */
public class JsoupTest {
    
    public static void parseString() {
        String html =
            "<html><head><title>blog</title></head><body onload='test(1)'><p onload='test(2)'>Parsed HTML into a doc.</p></body></html>";
        Document doc = Jsoup.parse(html);
        Elements es = doc.body().getAllElements();
        System.out.println(doc.title());
        for (Element element : es) {
            System.out.println(element.attr("onload"));
        }
    }
    
    /**
     * test maia
     * @param args
     */
    public static void main(String[] args) {
        parseString();
        //test
    }
}
