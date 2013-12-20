package com.lwying.music.fetch;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * fetch list and reply and get download urls
 * @author fullpanic
 *
 */
public class Fetch {
    private static Logger logger = Logger.getLogger(Fetch.class);
    
    private static final String ENCODING = "gb2312";
    
    private static final String MAIN = "http://www.zasv.com/";
    
    public static final String HOME_PAGE = "http://www.zasv.com/forum-74-1.html";
    
    private static final String LOGIN_URL =
        "http://www.zasv.com/member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LFWcH&inajax=1";
    
    private static final Pattern HTT_PATTERN = Pattern.compile("http://[\\w\\./-]+", Pattern.CASE_INSENSITIVE);
    
    private HttpContext localContext = null;
    
    private String formhash = "";
    
    private DefaultHttpClient client = new DefaultHttpClient();
    
    private String username;
    
    private String password;
    
    public Fetch(String name, String passwd, HttpContext context) {
        this.username = name;
        this.password = passwd;
        this.localContext = context;
    }
    
    public synchronized HttpResponse request(HttpRequestBase request, HttpClient client) {
        HttpResponse response = null;
        if (localContext == null) {
            CookieStore cookieStore = new BasicCookieStore();
            localContext = new BasicHttpContext();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        }
        try {
            //set agent
            request.setHeader("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2)");
            request.setHeader("Accept-Encoding", "deflate");
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            //            request.setHeader("Accept", "application/json, application/xml, text/html, text/*, image/*, */*");
            //response
            response = client.execute(request, localContext);
        }
        catch (Exception e) {
            logger.error("requst " + request.getURI() + " failed:", e);
        }
        return response;
    }
    
    public boolean login() {
        boolean rs = false;
        HttpPost method = new HttpPost(LOGIN_URL);
        InputStream ins = null;
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("username", this.username));
            nvps.add(new BasicNameValuePair("password", this.password));
            method.setEntity(new UrlEncodedFormEntity(nvps));
            HttpResponse response = request(method, client);
            ins = response.getEntity().getContent();
            String content = IOUtils.toString(ins, ENCODING);
            rs = !StringUtils.contains(content, "登录失败");
        }
        catch (Exception e) {
            logger.error("login failed:", e);
        }
        finally {
            IOUtils.closeQuietly(ins);
            if (method != null) {
                method.releaseConnection();
            }
        }
        return rs;
    }
    
    public Map<String, String> fetchPage(String url) {
        Map<String, String> map = new HashMap<String, String>();
        HttpGet get = new HttpGet(url);
        InputStream ins = null;
        try {
            HttpResponse res = request(get, client);
            ins = res.getEntity().getContent();
            Document doc = Jsoup.parse(ins, ENCODING, MAIN);
            Elements elements = doc.select("a[class=s xst]");
            if (elements != null) {
                for (Element node : elements) {
                    //                System.out.println(node.attr("href") + "|" + node.text());
                    map.put(node.attr("href"), node.text());
                    logger.debug(node.attr("href"));
                }
            }
            elements = doc.select("input[name=formhash]");
            if (elements != null) {
                formhash = elements.get(0).attr("value");
            }
            logger.info("get list:" + map.size());
        }
        catch (Exception e) {
            logger.error("fetch home page failed:", e);
        }
        finally {
            IOUtils.closeQuietly(ins);
            if (get != null) {
                get.releaseConnection();
            }
        }
        return map;
    }
    
    public String fetch2String(String url) {
        HttpGet get = null;
        InputStream ins = null;
        String content = null;
        try {
            get = new HttpGet(url);
            HttpResponse res = request(get, client);
            ins = res.getEntity().getContent();
            content = IOUtils.toString(ins, ENCODING);
        }
        catch (Exception e) {
            logger.error("fetch " + url + " failed:", e);
        }
        finally {
            IOUtils.closeQuietly(ins);
            if (get != null) {
                get.releaseConnection();
            }
        }
        return content;
    }
    
    public boolean reply(String url) {
        boolean rs = false;
        String content = null;
        HttpGet get = new HttpGet(url);
        InputStream ins = null;
        try {
            HttpResponse res = request(get, client);
            ins = res.getEntity().getContent();
            Document document = Jsoup.parse(ins, ENCODING, MAIN);
            Elements elements = document.select("a[onclick=showWindow('reply', this.href)]");
            if (elements != null) {
                //get reply url
                content = elements.get(0).absUrl("href");
                content = getReplyAction(content);
                logger.info("get reply action:" + content);
                elements = document.select("input[name=formhash]");
                if (elements != null) {
                    formhash = elements.get(0).attr("value");
                }
                //reply
                String title = getRandomPost(document, -1);
                logger.info(url + "->reply:" + title);
                rs = replyPost(content, title);
            }
        }
        catch (Exception e) {
            logger.error("fetch " + url + " failed:", e);
        }
        finally {
            IOUtils.closeQuietly(ins);
            if (get != null) {
                get.releaseConnection();
            }
        }
        return rs;
    }
    
    public String getRandomPost(Document document, int defalut) {
        String text = "谢谢LZ分享！！！！！！！！！！！！！！";
        Elements elements = document.select("div[class=t_fsz]");
        if (elements != null) {
            if (defalut != 0) {
                int size = elements.size();
                int index = RandomUtils.nextInt(size);
                index = index == 0 ? index + 1 : index;
                index = index == size ? index - 1 : index;
                text = elements.get(index).text();
            }
            else {
                text = elements.get(0).text();
            }
        }
        return text;
    }
    
    public String getReplyAction(String url) {
        HttpGet met = null;
        InputStream ins = null;
        String content = null;
        try {
            met = new HttpGet(url);
            HttpResponse res = request(met, client);
            ins = res.getEntity().getContent();
            Document document = Jsoup.parse(ins, ENCODING, MAIN);
            Elements elements = document.select("form[id=postform]");
            if (elements != null) {
                content = elements.get(0).absUrl("action");
            }
        }
        catch (Exception e) {
            logger.error("get replay action failed:", e);
        }
        finally {
            IOUtils.closeQuietly(ins);
            if (met != null) {
                met.releaseConnection();
            }
        }
        return content;
    }
    
    public boolean replyPost(String url, String content) {
        HttpPost post = null;
        boolean rs = false;
        InputStream ins = null;
        try {
            post = new HttpPost(url);
            //
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("message", content));
            nvps.add(new BasicNameValuePair("handlekey", "reply"));
            nvps.add(new BasicNameValuePair("formhash", formhash));
            post.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
            post.setHeader("Accept-Language", "en-US");
            HttpResponse response = request(post, client);
            ins = response.getEntity().getContent();
            String res = IOUtils.toString(ins, ENCODING);
            logger.info("post[" + url + "], rs:" + res);
            rs = res != null && res.length() < 10;
        }
        catch (Exception e) {
            logger.error("relpy post failed:", e);
        }
        finally {
            IOUtils.closeQuietly(ins);
            if (post != null) {
                post.releaseConnection();
            }
        }
        return rs;
    }
    
    public Map<String, Object> getHiden(String url) {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpGet get = null;
        InputStream ins = null;
        try {
            get = new HttpGet(url);
            HttpResponse response = request(get, client);
            ins = response.getEntity().getContent();
            String content = IOUtils.toString(ins, ENCODING);
            Document document = Jsoup.parse(content);
            Elements elements = document.select("div[class=showhide]");
            //detect download urls
            if (elements != null) {
                for (Element node : elements) {
                    String text = node.text();
                    Matcher matcher = HTT_PATTERN.matcher(text);
                    int index = 0;
                    while (matcher.find()) {
                        map.put(String.valueOf(index++), matcher.group());
                    }
                }
            }
            //detect password
            String text = getRandomPost(document, 0);
            map.put("text", text);
        }
        catch (Exception e) {
            logger.error("relpy post failed:", e);
        }
        finally {
            IOUtils.closeQuietly(ins);
            if (get != null) {
                get.releaseConnection();
            }
        }
        return map;
    }
}
