package storm.spider.spout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import storm.spider.SpiderClient;

/**
 * parse www.cnbeta.com, get all news' url list
 * @author lwying
 *
 * Sep 2, 2013
 */
public class CNBetaList {
    private static Logger logger = Logger.getLogger(CNBetaList.class);
    
    private static final String MAIN_URL = "http://www.cnbeta.com/";
    
    /**
     * get all articles from main url
     * @return all news urls
     */
    public List<String> process() {
        List<String> list = new ArrayList<String>();
        Executor executor = Executor.newInstance(SpiderClient.getClient());
        try {
            InputStream is = executor.execute(Request.Get(MAIN_URL)).returnContent().asStream();
            Document doc = Jsoup.parse(is, "UTF-8", MAIN_URL);
            Elements es = doc.body().getElementsByTag("dd");
            for (Element element : es) {
                //select by regex pattern
                Elements tElements = element.select("a[href~=.*articles/\\d+.htm]");
                if (tElements.size() > 0) {
                    for (Element e : tElements) {
                        //absUrl method, otherwise short urls can't be geted
                        String line = e.absUrl("href");
                        if (StringUtils.isNotEmpty(line)) {
                            list.add(line.trim());
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("process failed:", e);
        }
        return list;
    }
    
}
