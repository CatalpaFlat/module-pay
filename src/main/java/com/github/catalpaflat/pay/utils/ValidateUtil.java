package com.github.catalpaflat.pay.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.catalpaflat.pay.exception.CFPayException;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.github.catalpaflat.pay.constant.CFPayUrlConstant.HTTPS_VERIFY_URL;

/**
 * @author CatalpaFlat
 */
public final class ValidateUtil {
    private ValidateUtil() {
    }

    private boolean validatePayNotify(Map<String, String> params, String aliPublicKey, String appId, String partner) {
        // 使用支付宝公钥验签
        try {
            boolean isSignTrue = AlipaySignature.rsaCheckV1(params, aliPublicKey, "UTF-8", "RSA2");
            if (!isSignTrue) {
                return false;
            }
        } catch (AlipayApiException e1) {
            throw new CFPayException(e1.getMessage());
        }

        String notify_id = params.get("notify_id");
        // 验证
        if (StringUtils.isEmpty(notify_id)) {
            return false;
        }
        // 判断成功之后使用getResponse方法判断是否是支付宝发来的异步通知。
        if (!verifyResponse(notify_id, partner).equals("true")) {
            // 验证是否来自支付宝的通知失败
            return false;
        }
        if (!params.get("app_id").equals(appId)) {
            return false;
        }
        return true;
    }

    /**
     * 获取远程服务器ATN结果,验证返回URL
     *
     * @param notify_id 通知校验ID
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     * 返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    public String verifyResponse(String notify_id, String partner) {
        // 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求

        String veryfyUrl = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;

        return checkUrl(veryfyUrl);
    }

    /**
     * 获取远程服务器ATN结果
     *
     * @param urlvalue 指定URL路径地址
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     * 返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    public static String checkUrl(String urlvalue) {
        String inputLine = "";

        try {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            inputLine = in.readLine().toString();
        } catch (Exception e) {
            throw new CFPayException(e.getMessage());
        }

        return inputLine;
    }
}
