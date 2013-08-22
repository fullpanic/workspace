package com.test.lwying;

import java.io.File;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * @author lwying
 *
 * Aug 22, 2013
 * mongodb test case
 */
public class MongoDBTest {
    //mongo object
    private static Mongo mongo = null;
    
    private static DB db = null;
    
    private static DBCollection users = null;
    
    /**
     * init
     */
    public static void init() {
        try {
            //ip,port
            mongo = new Mongo("192.168.232.128", 27017);
            db = mongo.getDB("local");
            users = db.getCollection("test");
        }
        catch (Exception e) {
            System.err.println("init failed:" + e);
        }
    }
    
    /**
     * destroy
     */
    public static void close() {
        if (mongo != null) {
            mongo.close();
        }
        db = null;
        users = null;
    }
    
    /**
     * test mongondb's crud
     */
    public static void crudTest() {
        DBCursor cursor = users.find();
        System.out.println("all user count:" + users.count());
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
        //insert
        DBObject user = new BasicDBObject();
        user.put("username", "test1");
        user.put("age", "1");
        users.save(user);
        //print result
        System.out.println("all user count:" + users.count());
        //update
        users.update(new BasicDBObject("username", "test1"), new BasicDBObject("username", "fullpanic"));
        //query
        DBCursor rs = users.find(new BasicDBObject("username", "fullpanic"));
        while (rs.hasNext()) {
            System.out.println(rs.next());
        }
        //delete
        users.remove(new BasicDBObject("username", "fullpanic"));
        System.out.println("all user count:" + users.count());
    }
    
    /**
     * test fs
     */
    public static void testGridfs() {
        String filename = "/software/jd-gui.exe";
        try {
            GridFS fs = new GridFS(db);
            GridFSInputFile inputFile = fs.createFile(new File(filename));
            inputFile.save();
            DBCursor cursor = fs.getFileList();
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        }
        catch (Exception e) {
            System.err.println("test gridfs failed:" + e);
        }
    }
    
    /**
     * main method
     * @param args
     */
    public static void main(String[] args) {
        //init
        init();
        //test mongo
        //        crudTest();
        //test grid fs
        testGridfs();
        //clean
        close();
    }
}
