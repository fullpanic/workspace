package storm.spider.blot;

import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import storm.spider.utils.SpiderClient;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

/**
 * spider bolt:crawl url page to html document
 * @author lwying
 *
 * Sep 3, 2013
 */
public class SpiderBolt extends BaseRichBolt {
    private static Logger logger = Logger.getLogger(SpiderBolt.class);
    
    /**
     * auto generate serial id
     */
    private static final long serialVersionUID = -7090658693941941104L;
    
    public OutputCollector collector;
    
    private DefaultHttpClient client;
    
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.client = SpiderClient.getClient();
    }
    
    @Override
    public void execute(Tuple input) {
        String url = (String)input.getValue(0);
        logger.debug("fetch " + url);
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("crawl"));
    }
    
}
