package storm.spider;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

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
     * @param args
     * @return k-v map
     */
    private static Map<String, Object> parseCommandOptions(String[] args) {
        Options argOptions = new Options();
        argOptions.addOption(ConfigConsts.RunArgs.DB_NAME, true, "mongodb config");
        argOptions.addOption(ConfigConsts.RunArgs.PARALLEL, true, "parallel config");
        argOptions.addOption(ConfigConsts.RunArgs.TOP_NUM, true, "top url config");
        argOptions.addOption(ConfigConsts.RunArgs.SQS, true, "httpsqs config");
        CommandLineParser parser = new PosixParser();
        Map<String, Object> optsMap = new HashMap<String, Object>();
        try {
            CommandLine line = parser.parse(argOptions, args);
            Option[] opts = line.getOptions();
            //convert to map
            if (opts != null) {
                for (Option op : opts) {
                    optsMap.put(op.getOpt(), op.getValue());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
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
            Map<String, Object> runArgs = parseCommandOptions(args);
            
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
            
            //local submit
            if (args != null && args.length > 0) {
                conf.setNumWorkers(3);
                StormSubmitter.submitTopology(SpiderTopology.class.getSimpleName(), conf, builder.createTopology());
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
