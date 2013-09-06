package storm.spider.spout;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import storm.spider.ConfigConsts;
import storm.spider.crawler.CNBetaList;
import storm.spider.topology.SpiderTopology;
import storm.spider.utils.HttpSQSClient;

public class CNBetaListTest {
    
    public static enum CONSTS {
        LIST, ARRAY, MAP, SET
    }
    
    HttpSQSClient client = null;
    
    @Test
    public void test() {
        Map<String, Object> confMap = SpiderTopology.loadFromXML();
        client = HttpSQSClient.getClient(confMap);
        CNBetaList spider = new CNBetaList();
        
        List<String> list = spider.process();
        
        System.out.println(list);
        
        for (String s : list) {
            client.put(ConfigConsts.Queue.LIST, s);
        }
    }
    
    @Test
    public void testEnum() {
        System.out.println(CONSTS.LIST.toString());
    }
    
}
