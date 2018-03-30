package com.github.catalpaflat.pay.http;

import com.github.catalpaflat.pay.constant.EncodeConstant;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author CatalpaFlat
 */
public class HttpGetSupport extends HttpClientSupport {
    public static String get(String url) {
        return get(url, EncodeConstant.ENCODE_UTF_8, null, null);
    }

    public static String get(String url, String charset) {
        return get(url, charset, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIMR_OUT);
    }

    /**
     * get请求
     *
     * @param url         请求url
     * @param charset     编码
     * @param connTimeout 链接超时时间
     * @param readTimeout 响应读取超时时间
     * @return 响应
     */
    public static String get(String url, String charset, Integer connTimeout, Integer readTimeout) {

        HttpGet get = new HttpGet(url);
        String result = "";
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient client = null;
        try {
            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            get.setConfig(customReqConf.build());


            if (url.startsWith(URL_HTTPS)) {
                client = createSSLInsecureClient();
            } else {
                client = createDefaultHttpClient();
            }
            httpResponse = client.execute(get);
            result = IOUtils.toString(httpResponse.getEntity().getContent(), charset);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            get.releaseConnection();
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
