package storm.spider;

/**
 * define configuraion constances
 * @author lwying
 *
 * Sep 2, 2013
 */
public class ConfigConsts {
    
    public static final String SYS_ENCODE = "UTF-8";
    
    /**
     * run args define
     * @author lwying
     *
     * Sep 2, 2013
     */
    public static class RunArgs {
        //mongodb address
        public static final String DB_NAME = "db";
        
        //topology parallel num
        public static final String PARALLEL = "p";
        
        //crawl top num
        public static final String TOP_NUM = "top";
        
        //httpsqs config
        public static final String SQS = "sqs";
    }
    
    /**
     * httpsqs queue names
     * @author lwying
     *
     * Sep 3, 2013
     */
    public static class Queue {
        //url list queue
        public static final String LIST = "list";
    }
    
    /**
     * httpsqs opt status
     * @author lwying
     *
     * Sep 4, 2013
     */
    public static class HttpSQSCode {
        //get end
        public static final String GET_END = "HTTPSQS_GET_END";
    }
    
}
