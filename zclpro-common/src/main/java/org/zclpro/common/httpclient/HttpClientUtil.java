package org.zclpro.common.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class HttpClientUtil {

    /** httpclient相关参数设置，参考开涛博客：http://jinnianshilongnian.iteye.com/blog/2089792 */
    /**
     * 设置连接超时时间
     */
    public static final Integer CONNECTION_TIMEOUT = 2 * 1000; // 设置请求超时2秒钟
    public static final Integer SO_TIMEOUT = 2 * 1000; // 设置等待数据超时时间2秒钟

    /**
     * post请求,服务器端异常返回null
     *
     * @param url
     * @param param
     * @return string
     * @throws RuntimeException 请求异常
     */
    public static String postRequest(String url, Map<String, Object> param) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT)
                    .setConnectTimeout(CONNECTION_TIMEOUT).build();
            HttpPost method = new HttpPost(url);
            method.setConfig(requestConfig);
            if (param != null && !param.isEmpty()) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                for (Map.Entry<String, Object> each : param.entrySet()) {
                    formParams.add(new BasicNameValuePair(each.getKey(), each.getValue().toString()));
                }
                UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(formParams, "utf-8");
                method.setEntity(requestEntity);
            }
            CloseableHttpResponse response = httpClient.execute(method);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String httpGetJSON(String url, Map<String, Object> data) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpgets = null;
        String urlWithdata = null;
        try {
            urlWithdata = appentToUrl(url, data);
            log.debug("request " + urlWithdata);
            httpgets = new HttpGet(urlWithdata);
        } catch (UnsupportedEncodingException e1) {
            log.error("request " + urlWithdata + " fail", e1);
        }
        HttpResponse httpResponse;
        try {
            if (url.startsWith("https:")) {
                httpclient = createSSLInsecureClient();
            }
            httpResponse = httpclient.execute(httpgets);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String response = EntityUtils.toString(entity);
                log.debug("response String: " + response);
                return response;
            }
        } catch (IOException e) {
            log.error("request " + urlWithdata + " fail", e);
        } catch (GeneralSecurityException e) {
            log.error("request " + urlWithdata + " fail", e);
        } finally {
            try {
                httpclient.close();
            } catch (Exception e) {
                log.error("request " + urlWithdata + " fail", e);
            }
        }
        return null;

    }

    public static Map<String, String> httpGetJSONObject(String url, Map<String, Object> data) {
        String result = httpGetJSON(url, data);
        if (result != null) {
            return (Map<String, String>) JSON.parseObject(result, Map.class);
        } else {
            return null;
        }

    }

    private static String appentToUrl(String url, Map<String, Object> data) throws UnsupportedEncodingException {
        StringBuffer newUrl = new StringBuffer();
        newUrl.append(url);
        int i = 0;
        if (data != null) {
            for (Entry<String, Object> entry : data.entrySet()) {
                if (i == 0) {
                    newUrl.append("?" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "utf-8"));
                } else {
                    newUrl.append("&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "utf-8"));
                }
                i++;
            }
        }
        return newUrl.toString();
    }

    /**
     * 创建 SSL连接
     *
     * @return
     * @throws GeneralSecurityException
     */
    private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

            @Override
            public void verify(String host, SSLSocket ssl)
                    throws IOException {
            }

            @Override
            public void verify(String host, X509Certificate cert)
                    throws SSLException {
            }

            @Override
            public void verify(String host, String[] cns,
                               String[] subjectAlts) throws SSLException {
            }

        });

        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }
}