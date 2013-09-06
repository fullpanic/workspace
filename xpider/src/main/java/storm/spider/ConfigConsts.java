package storm.spider;

/**
 * define configuraion constances
 * @author lwying
 *
 * Sep 2, 2013
 */
public class ConfigConsts {
    /**
     * system encode
     */
    public static final String SYS_ENCODE = "UTF-8";
    
    /**
     * run args define
     * @author lwying
     *
     * Sep 2, 2013
     */
    public static class RunArgs {
        //mongodb address
        public static final String MONGO_IP = "mongo_ip";
        
        public static final String MONGO_PORT = "mongo_port";
        
        public static final String MONGO_USERNAME = "mongo_username";
        
        public static final String MONGO_PASSWD = "mongo_passwd";
        
        //topology parallel num
        public static final String PARALLEL = "parallel";
        
        //
        public static final String SQS_IP = "sqs_ip";
        
        public static final String SQS_PORT = "sqs_port";
        
        public static final String SQS_AUTH = "sqs_auth";
        
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
