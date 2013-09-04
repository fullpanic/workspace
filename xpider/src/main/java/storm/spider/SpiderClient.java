package storm.spider;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HttpContext;

/**
 * http client
 * @author lwying
 *
 * Sep 2, 2013
 */
public class SpiderClient {
    
    private static DefaultHttpClient httpClient = new DefaultHttpClient();
    
    /**
     * get http client
     **/
    public static DefaultHttpClient getClient() {
        if (httpClient != null) {
            return httpClient;
        }
        //init request headers
        httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 120000);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        httpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            
            @Override
            public void process(HttpRequest request, HttpContext context)
                throws HttpException, IOException {
                request.setHeader("Accept-Encoding", "gzip, deflate");
                request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                request.setHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
                request.setHeader("Connection", "keep-alive");
                request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0");
            }
        });
        
        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            
            @Override
            public void process(HttpResponse response, HttpContext context)
                throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    Header header = entity.getContentEncoding();
                    if (header != null) {
                        HeaderElement[] elements = header.getElements();
                        for (HeaderElement element : elements) {
                            if (element.getName().equalsIgnoreCase("gzip")) {
                                response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                                return;
                            }
                        }
                    }
                }
            }
        });
        return httpClient;
    }
}
