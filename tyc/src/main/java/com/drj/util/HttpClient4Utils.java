package com.drj.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.IOUtils;

import com.drj.reptile.ReptileDemo;

/**
 * 
 * @ClassName: HttpClient4Utils
 * @Description:TODO(Http 远程连接工具类)
 * @author: drj
 * @date: 2018年7月21日 下午9:18:19
 * 
 * @Copyright: 2018
 *
 */
public class HttpClient4Utils {
    public static HttpClient defaultClient = createHttpClient(20, 20, 5000, 5000, 3000);
    public static HttpClientContext context = HttpClientContext.create();
    public static String[] pool = {
            "TYCID=f70f9bc0d79e11e8b3e99fd5946c81d5; undefined=f70f9bc0d79e11e8b3e99fd5946c81d5; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1540393838,1540571099,1540572682; ssuid=6561370220; _ga=GA1.2.83818968.1540393840; jsid=SEM-SOUGOU-PP-SY-005932; _gid=GA1.2.1238005515.1540571101; RTYCID=fcb3e072a09d412f93f946f0ab7e9b23; CT_TYCID=d45b26b2fa94427a9f4f0e5a448eafe0; aliyungf_tc=AQAAAJ66MUvUAAIAS/pxe4itfT2MBiUf; csrfToken=7nZuPvyeX4fm46BQSVCBVhNA; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1540572749; tyc-user-info=%257B%2522myQuestionCount%2522%253A%25220%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522state%2522%253A%25220%2522%252C%2522vipManager%2522%253A%25220%2522%252C%2522onum%2522%253A%25220%2522%252C%2522monitorUnreadCount%2522%253A%25220%2522%252C%2522discussCommendCount%2522%253A%25220%2522%252C%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODIzNDQ4NDkxNCIsImlhdCI6MTU0MDU3Mjc0OSwiZXhwIjoxNTU2MTI0NzQ5fQ.FvOzwc7aw6ljvli35Q_x94D6ucyG9n8iUMdQliW-GeBkjsPx3sJGfrLZIBGtA15PxaFEksOLHxkUzQk9Szt0Tw%2522%252C%2522redPoint%2522%253A%25220%2522%252C%2522pleaseAnswerCount%2522%253A%25220%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522bizCardUnread%2522%253A%25220%2522%252C%2522mobile%2522%253A%252218234484914%2522%257D; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODIzNDQ4NDkxNCIsImlhdCI6MTU0MDU3Mjc0OSwiZXhwIjoxNTU2MTI0NzQ5fQ.FvOzwc7aw6ljvli35Q_x94D6ucyG9n8iUMdQliW-GeBkjsPx3sJGfrLZIBGtA15PxaFEksOLHxkUzQk9Szt0Tw; _gat_gtag_UA_123487620_1=1",
            "aliyungf_tc=AQAAAD4y8mshKQAAS/pxe+breiLLsYL0; csrfToken=D1cKyl7nNQhdBcKu3zeQjXia; TYCID=637fff60d93f11e8b3350de11d91b793; undefined=637fff60d93f11e8b3350de11d91b793; ssuid=8450581120; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1540572689; _ga=GA1.2.362628380.1540572689; _gid=GA1.2.1252243520.1540572689; tyc-user-info=%257B%2522myQuestionCount%2522%253A%25220%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522state%2522%253A%25220%2522%252C%2522vipManager%2522%253A%25220%2522%252C%2522onum%2522%253A%25220%2522%252C%2522monitorUnreadCount%2522%253A%25221%2522%252C%2522discussCommendCount%2522%253A%25220%2522%252C%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzMwMzQ0NzcxMiIsImlhdCI6MTU0MDU3MjgyMiwiZXhwIjoxNTU2MTI0ODIyfQ.Xxz_hsOey7IRP787X22En49IOG0zqy1Cm8LKFrVc18deGTAheAsZh7c5b4OYejAbpcTs4VpD5h4Onj_BkwyDhQ%2522%252C%2522redPoint%2522%253A%25220%2522%252C%2522pleaseAnswerCount%2522%253A%25221%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522bizCardUnread%2522%253A%25220%2522%252C%2522mobile%2522%253A%252213303447712%2522%257D; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzMwMzQ0NzcxMiIsImlhdCI6MTU0MDU3MjgyMiwiZXhwIjoxNTU2MTI0ODIyfQ.Xxz_hsOey7IRP787X22En49IOG0zqy1Cm8LKFrVc18deGTAheAsZh7c5b4OYejAbpcTs4VpD5h4Onj_BkwyDhQ; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1540572823; _gat_gtag_UA_123487620_1=1",
            "aliyungf_tc=AQAAALhViFiNyAsAS/pxezkKuSUtOiwr; csrfToken=mVwHiKZWdVPz-cdsMMatwANg; jsid=SEM-SOUGOU-PP-SY-005932; TYCID=1868dd20d94011e89c767bea6816a533; undefined=1868dd20d94011e89c767bea6816a533; ssuid=8077907912; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1540572992; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1540573035; _ga=GA1.2.1513650711.1540573011; _gid=GA1.2.501351945.1540573011; _gat_gtag_UA_123487620_1=1; tyc-user-info=%257B%2522myQuestionCount%2522%253A%25220%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522state%2522%253A%25220%2522%252C%2522vipManager%2522%253A%25220%2522%252C%2522onum%2522%253A%25220%2522%252C%2522monitorUnreadCount%2522%253A%25221%2522%252C%2522discussCommendCount%2522%253A%25220%2522%252C%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODgxMDMwMjAxOCIsImlhdCI6MTU0MDU3MzAyMCwiZXhwIjoxNTU2MTI1MDIwfQ.kvq2aWrqZ7mt71NI-2m23o_nMx8irAQV62kChwNYytI2IS1Qr83ePIyXJrjdnc8MNgkdUQ_qYou3qI5HQ9vRYg%2522%252C%2522redPoint%2522%253A%25220%2522%252C%2522pleaseAnswerCount%2522%253A%25220%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522bizCardUnread%2522%253A%25220%2522%252C%2522mobile%2522%253A%252218810302018%2522%257D; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODgxMDMwMjAxOCIsImlhdCI6MTU0MDU3MzAyMCwiZXhwIjoxNTU2MTI1MDIwfQ.kvq2aWrqZ7mt71NI-2m23o_nMx8irAQV62kChwNYytI2IS1Qr83ePIyXJrjdnc8MNgkdUQ_qYou3qI5HQ9vRYg",
            "TYCID=c0a20a30d75f11e89ed1d7849cd7256f; undefined=c0a20a30d75f11e89ed1d7849cd7256f; ssuid=2290229075; _ga=GA1.2.972028979.1540366692; tyc-user-info=%257B%2522myQuestionCount%2522%253A%25220%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522state%2522%253A%25220%2522%252C%2522vipManager%2522%253A%25220%2522%252C%2522onum%2522%253A%25220%2522%252C%2522monitorUnreadCount%2522%253A%252252%2522%252C%2522discussCommendCount%2522%253A%25220%2522%252C%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNTIyMjY5NjU5OCIsImlhdCI6MTU0MDM2NjcyNiwiZXhwIjoxNTU1OTE4NzI2fQ.epVhadBo4eOwNxpFwZ07jmj3ZebGDakLxWiYswL5xvcGMmATh8ZzdS8o3ZACy8Rd6-uYsyguoY3HMAZoHMSNlA%2522%252C%2522redPoint%2522%253A%25220%2522%252C%2522pleaseAnswerCount%2522%253A%25220%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522bizCardUnread%2522%253A%25220%2522%252C%2522mobile%2522%253A%252215222696598%2522%257D; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNTIyMjY5NjU5OCIsImlhdCI6MTU0MDM2NjcyNiwiZXhwIjoxNTU1OTE4NzI2fQ.epVhadBo4eOwNxpFwZ07jmj3ZebGDakLxWiYswL5xvcGMmATh8ZzdS8o3ZACy8Rd6-uYsyguoY3HMAZoHMSNlA; RTYCID=e13ccf807f1e490e9b6f5c253fd18c9b; CT_TYCID=a5ce91797a2840f1b695b7ddf8aeb9fa; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1540366691,1540535426; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1540535426; cloud_token=c400da5f7f50477baeb1e3528f1da830; cloud_utm=7aee0ef6e7534bae83a7f341eb45eaee; aliyungf_tc=AQAAAJIQ9lPUPQEA2n0natHJnDfMEI3e; csrfToken=YBAs1FABiJEKN7s6bdy1Jwc8; _gid=GA1.2.828286437.1540535426; _gat_gtag_UA_123487620_1=1" };

    /**
     * 实例化HttpClient
     *
     * @param maxTotal
     * @param maxPerRoute
     * @param socketTimeout
     * @param connectTimeout
     * @param connectionRequestTimeout
     * @return
     */
    public static HttpClient createHttpClient(int maxTotal, int maxPerRoute, int socketTimeout, int connectTimeout,
            int connectionRequestTimeout) {
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                //.setProxy(new HttpHost("61.128.208.94", 3128))// 代理服务Ip
                // .setProxy(new HttpHost("110.40.13.5", 80))
                .setConnectionRequestTimeout(connectionRequestTimeout).build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
        cm.setValidateAfterInactivity(200); // 一个连接idle超过200ms,再次被使用之前,需要先做validation
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
                .setConnectionTimeToLive(30, TimeUnit.SECONDS)
                .setRetryHandler(new StandardHttpRequestRetryHandler(3, true)) // 配置出错重试
                .setDefaultRequestConfig(defaultRequestConfig).build();

        startMonitorThread(cm);
        return httpClient;
    }

    /**
     * 增加定时任务, 每隔一段时间清理连接
     *
     * @param cm
     */
    private static void startMonitorThread(final PoolingHttpClientConnectionManager cm) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        cm.closeExpiredConnections();
                        cm.closeIdleConnections(30, TimeUnit.SECONDS);

                        // log.info("closing expired & idle connections, stat={}", cm.getTotalStats());
                        TimeUnit.SECONDS.sleep(10);
                    } catch (Exception e) {
                        // ignore exceptoin
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * 发送post请求
     *
     * @param httpClient
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param encoding
     *            编码
     * @return
     */
    public static String sendPost(HttpClient httpClient, String url, Map<String, String> params, Charset encoding) {
        String resp = "";
        HttpPost httpPost = new HttpPost(url);
        if (params != null && params.size() > 0) {// post 封装请求参数
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            Iterator<Map.Entry<String, String>> itr = params.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, String> entry = itr.next();
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(formParams, encoding);
            httpPost.setEntity(postEntity);
        }
        CloseableHttpResponse response = null;
        try {
            response = (CloseableHttpResponse) httpClient.execute(httpPost);
            resp = EntityUtils.toString(response.getEntity(), encoding);
        } catch (Exception e) {
            // log
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // log
                    e.printStackTrace();
                }
            }
        }
        return resp;
    }

    public static String sendGet(HttpClient httpClient, String url, Map<String, Object> params, Charset encoding) {
        String resp = "";
        StringBuilder buffer = new StringBuilder();
        if (params != null && params.size() > 0) {// get 封装请求参数
            buffer.append("?");
            Iterator<Map.Entry<String, Object>> itr = params.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, Object> entry = itr.next();
                buffer.append(entry.getKey());
                buffer.append("=");
                buffer.append(entry.getValue());
                if (itr.hasNext()) {
                    buffer.append("&");
                }
            }
        }
        HttpGet httpPost = new HttpGet(url + buffer);
        setHttpHeaders(httpPost);// 设置请求头
        CloseableHttpResponse response = null;
        try {
            response = (CloseableHttpResponse) httpClient.execute(httpPost);
            if(ReptileDemo.getCheckUrl(response,context)) {
            	 resp = EntityUtils.toString(response.getEntity(), encoding);
            }
        } catch (Exception e) {
            // log
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // log
                    e.printStackTrace();
                }
            }
        }
        return resp;
    }

    public static void setHttpHeaders(HttpGet httpGet) {
        Random r = new Random();
        int i = r.nextInt(3);
        System.err.println("pool is" + i);
        httpGet.setHeader("Cookie", pool[i]);
        httpGet.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Host", "www.tianyancha.com");
        httpGet.setHeader("Referer", "https://www.tianyancha.com/");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("User-Agent", StringUtils.getHeaderInfo());
        //
        // Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like
        // Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134
    }

    /**
     * 发送post请求
     *
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @return
     */
    public static String sendPost(String url, Map<String, Object> params) {
        Charset encoding = Charset.forName("gbk");
        return sendGet(defaultClient, url, params, encoding);
    }

    public static void main(String[] args) {
    }
}
