package storm.spider.spout;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import storm.spider.ConfigConsts;
import storm.spider.utils.DBUtils;
import storm.spider.utils.HttpSQSClient;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * spider spout, using for crawling
 * @author lwying
 *
 * Sep 2, 2013
 */
public class SpiderSpout extends BaseRichSpout {
    private static Logger logger = Logger.getLogger(SpiderSpout.class);
    
    /**
     * auto generate serial number
     */
    private static final long serialVersionUID = -7832207209087810928L;
    
    public SpoutOutputCollector collector;
    
    private HttpSQSClient client;
    
    private String queueName;
    
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        DBUtils.init(conf);
        client = HttpSQSClient.getClient(conf);
        queueName = ConfigConsts.Queue.LIST;
    }
    
    @Override
    public void nextTuple() {
        String msg = client.get(queueName);
        
        if (StringUtils.isEmpty(msg) || StringUtils.endsWithIgnoreCase(ConfigConsts.HttpSQSCode.GET_END, msg)) {
            logger.info("message is null and sleep for 1000ms");
            Utils.sleep(1000);
            return;
        }
        logger.debug("queue[" + queueName + "] get msg[" + msg + "]");
        
        collector.emit(new Values(msg));
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("url"));
    }
    
}
