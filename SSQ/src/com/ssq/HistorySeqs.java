package com.ssq;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * get ssq history seqs from 'http://kaijiang.zhcw.com/zhcw/html/ssq/list_1.html'
 * @author fullpanic
 *
 */
public class HistorySeqs {
    
    public static final int N = 70;
    
    public static final String PATH_STRING = "./testcase/res.txt";
    
    public static String urlString = "http://kaijiang.zhcw.com/zhcw/html/ssq/list_%d.html";
    
    public static final String HOME = "http://kaijiang.zhcw.com/zhcw/html/ssq/list_1.html";
    
    public static int count = 0;
    
    public static List<String> list = new ArrayList<String>();
    
    private static int getTotalPages(String url) {
        int t = N;
        try {
            Document doc = Jsoup.parse(new URL(url), 10000);
            Element es = doc.body().select("p[class=pg]").select("strong").first();
            String string = es.text();
            t = Integer.parseInt(string);
        }
        catch (Exception e) {
            System.err.println("parse failed:" + e);
        }
        return t;
    }
    
    public static List<String> parseURL(int size) {
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= size; i++) {
            try {
                System.out.println("get page " + i);
                Document doc = Jsoup.parse(new URL(String.format(urlString, new Object[] {Integer.valueOf(i)})), 10000);
                Elements es = doc.body().getElementsByTag("td");
                for (Element element : es) {
                    Elements tElements = element.select("td[style=padding-left:10px;]");
                    if (tElements.size() > 0) {
                        String line = tElements.text();
                        if (StringUtils.isNotEmpty(line)) {
                            count += 1;
                            list.add(line.trim());
                        }
                    }
                }
                
                Thread.sleep(200L);
            }
            catch (Exception e) {
                System.err.println("parse failed:" + e);
            }
        }
        return list;
    }
    
    public static void main(String[] args) {
        int size = 5;
        if (args != null) {
            if (StringUtils.isNumeric(args[0])) {
                size = Integer.parseInt(args[0]);
            }
            else {
                size = getTotalPages(HOME);
            }
        }
        else {
            size = getTotalPages(HOME);
        }
        System.out.println("start to retrieve...");
        list = parseURL(size);
        System.out.println("total:" + count);
        try {
            FileUtils.writeLines(new File(PATH_STRING), list);
        }
        catch (IOException e) {
        }
    }
}
