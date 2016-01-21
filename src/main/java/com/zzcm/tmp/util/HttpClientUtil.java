package com.zzcm.tmp.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;

/**
 * 
 * @author HuYing
 * @date 2015年12月7日
 */
public class HttpClientUtil {
    private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	private static PoolingHttpClientConnectionManager connManager = null;
    private static CloseableHttpClient httpclient = null;
    
    static{
    	ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf)
                .register("https", sslsf)
                .build();
        connManager = new PoolingHttpClientConnectionManager(registry);
        //请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试                    
                    return true;
                }
                if(exception instanceof SocketTimeoutException){
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常                    
                    return false;
                }                
                if (exception instanceof InterruptedIOException) {// 超时                    
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达                    
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝                    
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常                    
                    return false;
                }
                 
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {                    
                    return true;
                }
                return false;
            }
        };

        //BasicCookieStore cookieStore = new BasicCookieStore();
        //CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
        //    public CookieSpec create(HttpContext context) {
        //
        //        return new BrowserCompatSpec() {
        //            @Override
        //            public void validate(Cookie cookie, CookieOrigin origin)
        //                    throws MalformedCookieException {
        //                    // Oh, I am easy
        //            }
        //        };
        //    }
        //
        //};
        //Registry<CookieSpecProvider> r = RegistryBuilder
        //        .<CookieSpecProvider> create()
        //        .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
        //        .register(CookieSpecs.BROWSER_COMPATIBILITY,
        //                new BrowserCompatSpecFactory())
        //        .register("easy", easySpecProvider).build();


        RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .setSocketTimeout(20000)
                .setStaleConnectionCheckEnabled(true)
                .build();

        // Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
        connManager.setDefaultSocketConfig(socketConfig);
        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
            .setMaxHeaderCount(200)
            .setMaxLineLength(2000)
            .build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .setMessageConstraints(messageConstraints)
            .build();

        connManager.setDefaultConnectionConfig(connectionConfig);
        // 将最大连接数增加到200
        connManager.setMaxTotal(1000);
        // 将每个路由基础的连接增加到20
        connManager.setDefaultMaxPerRoute(connManager.getMaxTotal());

        httpclient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setRetryHandler(httpRequestRetryHandler)
                //.setRetryHandler(new DefaultHttpRequestRetryHandler(5, true))
                //.setConnectionReuseStrategy(new DefaultConnectionReuseStrategy())
                //.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setDefaultRequestConfig(customizedRequestConfig)
                        //.setDefaultCookieSpecRegistry(r)
                        //.setDefaultCookieStore(cookieStore)
                .build();
    }
    
    public static String get(String url){
    	CloseableHttpResponse response = null;
    	HttpGet get = new HttpGet(url);
    	try {
    		response = httpclient.execute(get);
            if(null==response || null==response.getStatusLine()){
                logger.error("get[" + url + "] failed.");
                return null;
            }
            if(response.getStatusLine().getStatusCode()!=200){
                logger.error("http get["+url+"] failed:"+response.getStatusLine());
                return null;
            }
            HttpEntity entity = response.getEntity();
            try {
                String res = EntityUtils.toString(entity,"utf-8");
                return res;
            } catch (IOException e) {
                logger.error("get[" + url + "] failed:", e);
            } catch (ParseException e) {
                logger.error("get[" + url + "] failed:",e);
            } finally {
                EntityUtils.consume(entity);
                if(response != null){
                    response.close();
                }
            }
        } catch (ClientProtocolException e) {
            logger.error("get["+url+"] failed:",e);
        } catch (IOException e) {
            logger.error("get["+url+"] failed:",e);
		} finally {
            get.releaseConnection();
        }
        return null;
    }
    
    public static void main(String[] args) {
        int num = 1000;
    	Thread[] ts = new Thread[num];
        for (int i = 0; i < num; i++) {
            ts[i] = new Thread(){
                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        String url = "https://www.baidu.com/";
                        //String url = "http://www.360.com/";
                        //if(j%2==0){
                        //    url = "http://www.baidu.com/";
                        //}else{
                        //    //url = "https://github.com/";
                        //}
                        String res = HttpClientUtil.get(url);
                        if(StringUtils.isEmpty(res)){
                            System.err.println("null!!!!");
                        }
                    }
                }
            };
        }

        for (Thread t : ts) {
            t.start();
        }



	}
    
}
