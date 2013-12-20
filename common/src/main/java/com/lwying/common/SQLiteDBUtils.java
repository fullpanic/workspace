package com.lwying.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author fullpanic
 *
 */
public class SQLiteDBUtils {
    private static Logger logger = Logger.getLogger(SQLiteDBUtils.class);
    
    private static volatile boolean isInit = false;
    
    private static Connection connection = null;
    
    private String dbpath = "db.s3db";
    
    public String getDbpath() {
        return dbpath;
    }
    
    public void setDbpath(String dbpath) {
        this.dbpath = dbpath;
    }
    
    public void init() {
        synchronized (SQLiteDBUtils.class) {
            if (!isInit) {
                try {
                    Class.forName("org.sqlite.JDBC");
                    connection = DriverManager.getConnection("jdbc:sqlite:" + dbpath);
                }
                catch (Exception e) {
                    logger.error("init failed:", e);
                }
                isInit = true;
            }
        }
    }
    
    public static void close(Statement statement, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        catch (Exception e) {
            logger.error("close failed:", e);
        }
    }
    
    public static List<Map<String, Object>> query(String sql, Object... args) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            logger.debug("query[" + sql + "], params[" + Arrays.toString(args) + "]");
            ps = connection.prepareStatement(sql);
            int index = 1;
            if (args != null) {
                for (Object s : args) {
                    ps.setObject(index++, s);
                }
            }
            //query
            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    map.put(rs.getMetaData().getColumnName(i).toLowerCase(), rs.getObject(i));
                }
                list.add(map);
            }
        }
        catch (Exception e) {
            logger.error("query[" + sql + "] failed:", e);
        }
        finally {
            close(ps, rs);
        }
        return list;
    }
}
