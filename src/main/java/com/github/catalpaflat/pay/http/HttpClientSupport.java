package com.github.catalpaflat.pay.http;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

/**
 * @author CatalpaFlat
 */
public class HttpClientSupport {

    public static final int DEFAULT_CONNECT_TIME_OUT = 10000;
    public static final int DEFAULT_READ_TIMR_OUT = 10000;
    static final String URL_HTTPS = "https";
    static final String CONTENT_TYPE = "Content-Type";

    static CloseableHttpClient createDefaultHttpClient() {
        return HttpClientBuilder.create().build();
    }

    /**
     * 创建SSL请求
     *
     * @return CloseableHttpClient
     * @throws GeneralSecurityException 异常
     */
    static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {

                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }


                public void verify(String host, SSLSocket ssl)
                        throws IOException {
                }

                public void verify(String host, X509Certificate cert)
                        throws SSLException {
                }

                public void verify(String host, String[] cns,
                                   String[] subjectAlts) throws SSLException {
                }

            });


            return HttpClients.custom().setSSLSocketFactory(sslsf).build();

        } catch (GeneralSecurityException e) {
            throw e;
        }
    }

    static String executePostRequestWIthParseResponse(String url, String charset, HttpPost post) {
        String result = "";
        CloseableHttpResponse httpResponse = null;

        CloseableHttpClient client = null;
        try {
            if (url.startsWith(URL_HTTPS)) {
                client = createSSLInsecureClient();
            } else {
                client = createDefaultHttpClient();
            }

            httpResponse = client.execute(post);

            result = IOUtils.toString(httpResponse.getEntity().getContent(), charset);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
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
