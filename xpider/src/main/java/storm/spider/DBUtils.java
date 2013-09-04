package storm.spider;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * mongodb operation utils class
 * 
 * @author lwying
 *
 * Sep 2, 2013
 */
public class DBUtils {
    
    private static Logger logger = Logger.getLogger(DBUtils.class);
    
    private static Mongo mongo = null;
    
    /**
     * init mongdb with ip and port
     * @param host server info like ip:port
     * @return true or false
     */
    public synchronized static boolean init(String host) {
        if (mongo != null) {
            return true;
        }
        logger.debug("init mongodb [" + host + "]");
        boolean status = false;
        try {
            String[] strs = host.split(":");
            mongo = new Mongo(strs[0], Integer.valueOf(strs[1]));
            status = true;
        }
        catch (Exception e) {
            logger.error("init mongodb failed:", e);
        }
        return status;
    }
    
    /**
     * save obj to database
     * @param db db name
     * @param coll collection name
     * @param obj save obj
     */
    public synchronized static void save(String db, String coll, DBObject obj) {
        logger.debug("save[" + obj + "] to " + db + "'s " + coll);
        DB dbObj = mongo.getDB(db);
        DBCollection dbcoll = dbObj.getCollection(coll);
        dbcoll.save(obj);
    }
}
