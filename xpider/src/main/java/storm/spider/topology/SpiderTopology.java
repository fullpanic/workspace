package storm.spider.topology;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import storm.spider.ConfigConsts;
import storm.spider.blot.SpiderBolt;
import storm.spider.spout.SpiderSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

/**
 * crawl www.cnbeta.com to get news list and send to mongodb
 * 
 * @author lwying
 *
 * Sep 2, 2013
 */
public class SpiderTopology {
    private static Logger logger = Logger.getLogger(SpiderTopology.class);
    
    /**
     * convert run args to k-v, like as:<br>
     * -p parallelNum<br>
     * -db ip:port<br>
     * -top max url num<br>
     * @return k-v map
     */
    public static Map<String, Object> loadFromXML() {
        Map<String, Object> optsMap = new HashMap<String, Object>();
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:conf/application_config.xml");
        Properties properties = (Properties)ac.getBean("config");
        for (Entry<Object, Object> en : properties.entrySet()) {
            String key = (String)en.getKey();
            optsMap.put(key, en.getValue());
        }
        return optsMap;
    }
    
    /**
     * topology main method
     * @param args run args
     */
    public static void main(String[] args) {
        try {
            //get run args
            Map<String, Object> runArgs = loadFromXML();
            
            logger.debug(runArgs);
            
            String par = (String)runArgs.get(ConfigConsts.RunArgs.PARALLEL);
            
            int p = Integer.valueOf(par);
            
            TopologyBuilder builder = new TopologyBuilder();
            //set crawl spout
            builder.setSpout("job", new SpiderSpout(), p);
            //set crawl bolt
            builder.setBolt("crawl", new SpiderBolt()).shuffleGrouping("job");
            //set snapshot parse bolt
            
            //set parse result sending to mongodb bolt
            
            //topology configuration
            Config conf = new Config();
            conf.putAll(runArgs);
            conf.setDebug(true);
            
            //local submit,args[0]=task name
            if (args != null && args.length > 0) {
                conf.setNumWorkers(3);
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            }
            else {//cluster submit
                conf.setMaxTaskParallelism(3);
                LocalCluster cluster = new LocalCluster();
                cluster.submitTopology(SpiderTopology.class.getSimpleName(), conf, builder.createTopology());
                Thread.sleep(10000);
                cluster.shutdown();
            }
        }
        catch (Exception e) {
            logger.error("spider topology failed:", e);
        }
    }
}
