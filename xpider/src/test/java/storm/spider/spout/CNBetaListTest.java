package storm.spider.spout;

import java.util.List;

import org.junit.Test;

import storm.spider.ConfigConsts;
import storm.spider.HttpSQSClient;

public class CNBetaListTest {
    
    public static enum CONSTS {
        LIST, ARRAY, MAP, SET
    }
    
    HttpSQSClient client = null;
    
    @Test
    public void test() {
        client = HttpSQSClient.getClient("123456@192.168.3.245:1218");
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
