package io.github.tuhe32.bin.pay.allin.util;

import io.github.tuhe32.bin.pay.allin.constants.AllinPayConstant;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.*;

/**
 * @author 刘斌
 * @date 2024/5/7 16:57
 */
public class SignUtils {

    private static final String SIGN_TYPE_RSA = "RSA";

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();


    public static String sign(Map<String, String> params, String appKey, String signType) throws PayException {
        if (params == null || params.isEmpty()) {
            throw new PayException("签名参数不能为空");
        }
        params.remove("sign");
        if (!SIGN_TYPE_RSA.equals(signType)) {
            throw new PayException("不支持的通联签名类型");
        }
        try {
            String signStr = buildSignStr(params);
            return sha1withRSASignature(appKey, signStr);
        } catch (Exception e) {
            throw new PayException("通联签名计算出现异常", e);
        }
    }

    /**
     * 私钥签名
     *
     * @param privateKeyStr RSA私钥
     * @param dataStr       待加签数据
     * @return 签名
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public static String sha1withRSASignature(String privateKeyStr, String dataStr) {
        try {
            byte[] key = Base64.getDecoder().decode(privateKeyStr);
            byte[] data = dataStr.getBytes(StandardCharsets.UTF_8);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return new String(Base64.getEncoder().encode(signature.sign()));
        } catch (Exception e) {
            throw new RuntimeException("签名计算出现异常", e);
        }
    }

    public static void validSign(Map<String, String> params) throws PayException {
        if (params == null || params.isEmpty()) {
            throw new PayException("验签失败，参数为空");
        }
        if (!params.containsKey("sign")) {
            throw new PayException("验签失败，签名为空");
        }
        String sign = params.remove("sign");
        try {
            String buildSignStr = buildSignStr(params);
            boolean verifySignature = rsaVerifySignature(buildSignStr, AllinPayConstant.RSA_PUBLIC_KEY, sign);
            if (!verifySignature) {
                throw new PayException("通联签名验证失败");
            }
        } catch (Exception e) {
            throw (e instanceof PayException) ? (PayException) e : new PayException("通联验签出现异常", e);
        }
    }

    /**
     * 公钥验签
     *
     * @param dataStr      待验签数据
     * @param publicKeyStr RSA公钥
     * @param signStr      签名
     * @return 验签成功/失败
     * @throws Exception 验签异常
     */
    public static boolean rsaVerifySignature(String dataStr, String publicKeyStr, String signStr) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(publicKey);
        signature.update(dataStr.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.getDecoder().decode(signStr));
    }

    /**
     * 生成随机码
     *
     * @param n 随机码位数
     * @return 随机码
     */
    public static String randomCode(int n) {
        Random random = new Random();
        StringBuilder sRand = new StringBuilder();
        // default 4
        n = n == 0 ? 4 : n;
        for (int i = 0; i < n; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
        }
        return sRand.toString();
    }

    public static String buildSignStr(Map<String, String> params) {
        Set<String> keySet = params.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String key : keyArray) {
//        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = params.get(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            sb.append(key).append("=").append(value)
                    .append("&");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String send(String url, String requestStr) throws PayException {
        // 创建HttpClient实例
//        HttpClient httpClient = HttpClient.newBuilder()
//                .version(HttpClient.Version.HTTP_1_1)
//                .connectTimeout(Duration.ofSeconds(10))
//                .build();
        // 请求的URI
        URI uri = URI.create(url);
        try {
            // 构建GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(requestStr))
                    .build();

            // 发送请求并处理响应
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            return response.body();
            // 检查响应状态码
//            if (response.statusCode() == 200) {
//                System.out.println("Response: " + response.body());
//            } else {
//                System.err.println("Unexpected status code: " + response.statusCode());
//            }
        } catch (IOException | InterruptedException e) {
            throw new PayException("An error occurred while sending the HTTP request: " + e.getMessage(), e);
        }
    }

    public static String buildRequestStr(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        Set<String> keySet = params.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String key : keyArray) {
//        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = params.get(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            sb.append(key).append("=").append(URLEncoder.encode(value, StandardCharsets.UTF_8))
                    .append("&");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
