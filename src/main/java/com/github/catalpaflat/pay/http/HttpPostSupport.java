package com.github.catalpaflat.pay.http;

import com.github.catalpaflat.pay.constant.EncodeConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author CatalpaFlat
 */
public class HttpPostSupport extends HttpClientSupport {

    public static String post(String url, Map<String, String> params) {
        return postForm(url, EncodeConstant.ENCODE_UTF_8, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIMR_OUT,
                MediaType.APPLICATION_FORM_URLENCODED.getType(), params, null);
    }

    public static String post(String url, String body) {
        return postBody(url, body, EncodeConstant.ENCODE_UTF_8, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIMR_OUT,
                MediaType.APPLICATION_FORM_URLENCODED.getType(), null);
    }

    public static String post(String url, Map<String, String> params, Integer connTimeout, Integer readTimeout) {
        return postForm(url, EncodeConstant.ENCODE_UTF_8, connTimeout, readTimeout,
                MediaType.APPLICATION_FORM_URLENCODED.getType(), params, null);
    }

    /**
     * post 请求raw-body格式
     *
     * @param url                请求url
     * @param body               请求体
     * @param charset            编码
     * @param connTimeout        链接超时时间
     * @param readTimeout        响应读取超时时间
     * @param defaultContentType 请求头 application/xml "application/x-www-form-urlencoded"
     * @param additionalHeaders  附加请求头信息
     * @return 响应
     */
    public static String postBody(String url, String body, String charset, Integer connTimeout, Integer readTimeout,
                                  String defaultContentType, Map<String, String> additionalHeaders) {

        HttpPost post = new HttpPost(url);
        post.setHeader(CONTENT_TYPE, defaultContentType);
        if (additionalHeaders !=null){
            for (Map.Entry<String, String> map : additionalHeaders.entrySet()) {
                post.setHeader(map.getKey(), map.getValue());
            }
        }

        if (StringUtils.isNotBlank(body)) {
            StringEntity s = new StringEntity(body, ContentType.create(defaultContentType, charset));
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, defaultContentType));
            post.setEntity(s);
        }
        // 设置参数
        RequestConfig.Builder customReqConf = RequestConfig.custom();
        if (connTimeout != null) {
            customReqConf.setConnectTimeout(connTimeout);
        }
        if (readTimeout != null) {
            customReqConf.setSocketTimeout(readTimeout);
        }
        return executePostRequestWIthParseResponse(url, charset, post);
    }

    /**
     * post请求 form表达提交
     *
     * @param url                请求url
     * @param charset            编码
     * @param connTimeout        链接超时时间
     * @param readTimeout        响应读取超时时间
     * @param defaultContentType 请求头 application/xml "application/x-www-form-urlencoded"
     * @param params             form参数
     * @param headers            附加请求头信息
     * @return 响应
     */
    public static String postForm(String url, String charset, Integer connTimeout, Integer readTimeout, String defaultContentType
            , Map<String, String> params, Map<String, String> headers) {

        HttpPost post = new HttpPost(url);
        post.setHeader(CONTENT_TYPE, defaultContentType);

        if (params != null && !params.isEmpty()) {
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
            post.setEntity(entity);
        }

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                post.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 设置参数
        RequestConfig.Builder customReqConf = RequestConfig.custom();
        if (connTimeout != null) {
            customReqConf.setConnectTimeout(connTimeout);
        }
        if (readTimeout != null) {
            customReqConf.setSocketTimeout(readTimeout);
        }
        post.setConfig(customReqConf.build());

        return executePostRequestWIthParseResponse(url, charset, post);
    }
}
