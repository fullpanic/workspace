package com.lwying.music.fetch;

import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

public class FetchTest {
    
    @Test
    public void testlogin()
        throws Exception {
        boolean rs = Fetch.login();
        Assert.assertTrue(rs);
    }
    
    @Test
    public void testFetch()
        throws Exception {
        Fetch.login();
        //        Fetch.getSession();
        Fetch.fetchPage("http://www.zasv.com/forum-74-2.html");
        //        String contentString = Fetch.reply("http://www.zasv.com/thread-120513-1-10.html");
        //        System.out.println(contentString);
    }
    
    @Test
    public void getPage()
        throws Exception {
        //        Fetch.login();
        String url = "http://www.zasv.com/thread-120513-1-10.html";
        url = Fetch.fetch2String(url);
        //        FileUtils.writeStringToFile(new File("1.txt"), url);
        Document document = Jsoup.parse(url);
        Elements elements = document.select("div[class=t_fsz]");
        if (elements != null) {
            int size = elements.size();
            size = RandomUtils.nextInt(size);
            size = size == 0 ? size + 1 : size;
            String text = elements.get(size).text();
            System.out.println(text.substring(0, 20));
        }
    }
    
    @Test
    public void testReply() {
        Fetch.login();
        String url = "http://www.zasv.com/thread-2523153-1-1.html";
        List<String> hides = Fetch.getHiden(url);
        if (hides.size() == 0) {
            boolean rs = Fetch.reply(url);
            if (rs) {
                hides = Fetch.getHiden(url);
            }
            else {
                //
            }
        }
        System.out.println(hides);
    }
}
