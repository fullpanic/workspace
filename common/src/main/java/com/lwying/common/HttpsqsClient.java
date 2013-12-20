package com.lwying.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class HttpsqsClient {
    
    //    private static Logger logger = Logger.getLogger(HttpsqsClient.class);
    
    // private String server, port, charset,auth;
    private String host;
    
    private Proxy proxy;
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    private String doprocess(String urlstr) {
        URL url = null;
        try {
            url = new URL(urlstr);
        }
        catch (MalformedURLException e) {
            return "The httpsqs server must be error";
        }
        
        BufferedReader instream = null;
        try {
            
            URLConnection conn;
            if (this.proxy != null) {
                conn = url.openConnection(this.proxy);
            }
            else {
                conn = url.openConnection();
            }
            instream = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            String s = null;
            StringBuffer result = new StringBuffer("");
            
            while ((s = instream.readLine()) != null) {
                result.append(s);
            }
            instream.close();
            return result.toString();
        }
        catch (IOException e) {
            return "Get data error";
        }
        finally {
            IOUtils.closeQuietly(instream);
        }
    }
    
    public String maxqueue(String queue_name, String num) {
        String urlstr = this.host + "&name=" + queue_name + "&opt=maxqueue&num=" + num;
        String result = null;
        
        result = this.doprocess(urlstr);
        return result;
    }
    
    public String reset(String queue_name) {
        String urlstr = this.host + "&name=" + queue_name + "&opt=reset";
        String result = null;
        
        result = this.doprocess(urlstr);
        return result;
    }
    
    public String view(String queue_name, String pos) {
        String urlstr = this.host + "&name=" + queue_name + "&opt=view&pos=" + pos;
        String result = null;
        
        result = this.doprocess(urlstr);
        return result;
    }
    
    public String status(String queue_name) {
        String urlstr = this.host + "&name=" + queue_name + "&opt=status";
        String result = null;
        
        result = this.doprocess(urlstr);
        return result;
    }
    
    public String get(String queue_name) {
        String urlstr = this.host + "&name=" + queue_name + "&opt=get";
        String result = null;
        
        result = this.doprocess(urlstr);
        return result;
    }
    
    public String put(String queue_name, String data) {
        String urlstr = this.host + "&name=" + queue_name + "&opt=put";
        URL url = null;
        try {
            url = new URL(urlstr);
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("The httpsqs server must be error", e);
        }
        
        URLConnection conn = null;
        OutputStreamWriter out = null;
        try {
            
            if (this.proxy != null) {
                conn = url.openConnection(this.proxy);
            }
            else {
                conn = url.openConnection();
            }
            
            conn.setDoOutput(true);
            
            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(data);
            out.flush();
            out.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Put data error", e);
        }
        finally {
            IOUtils.closeQuietly(out);
        }
        
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                return line;
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Get return data error", e);
        }
        finally {
            IOUtils.closeQuietly(reader);
        }
        return null;
    }
    
    public boolean isEnd(String data) {
        return StringUtils.equalsIgnoreCase(data, Consts.SQS_END);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HttpsqsClient [host=");
        builder.append(host);
        builder.append("]");
        return builder.toString();
    }
}
