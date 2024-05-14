package io.github.tuhe32.bin.pay.ums.util;

import io.github.tuhe32.bin.pay.common.exception.PayException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 刘斌
 * @date 2024/5/11 17:14
 */
public class UmsSignUtils {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static String openBodySig(String body, String appId, String appKey) throws Exception {
        String timestamp = getCurrentTime("yyyyMMddHHmmss");
        String nonce = UUID.randomUUID().toString().replace("-", "");
        byte[] data = body.getBytes(StandardCharsets.UTF_8);

        InputStream is = new ByteArrayInputStream(data);
        String bodyDigest = DigestUtils.sha256Hex(is);
        String str1C = appId + timestamp + nonce + bodyDigest;

        byte[] localSignature = hmacSHA256(str1C.getBytes(), appKey.getBytes());

        // Signature
        String localSignatureStr = Base64.encodeBase64String(localSignature);
        return ("OPEN-BODY-SIG AppId=" + "\"" + appId + "\"" + ", Timestamp=" + "\"" + timestamp + "\"" + ", Nonce=" + "\"" + nonce + "\"" + ", Signature=" + "\"" + localSignatureStr + "\"");
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public static byte[] hmacSHA256(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data);
    }

    public static void validSign(Map<String, String> params, String md5Key) throws PayException {
        if (params == null || params.isEmpty()) {
            throw new PayException("验签失败，参数为空");
        }
        if (!params.containsKey("sign")) {
            throw new PayException("验签失败，签名为空");
        }
        String sign = params.remove("sign");
        try {
            String buildSignStr = buildSignStr(params);
            String text = buildSignStr + md5Key;
            String genSign = DigestUtils.sha256Hex(text.getBytes(StandardCharsets.UTF_8)).toUpperCase();
            boolean verifySignature = sign.equals(genSign);
            if (!verifySignature) {
                throw new PayException("银商签名验证失败");
            }
        } catch (Exception e) {
            throw (e instanceof PayException) ? (PayException) e : new PayException("银商验签出现异常", e);
        }
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

    public static String buildUrlStr(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
//        for (String key : keyArray) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            if (StringUtils.isBlank(value)) {
                continue;
            }
            sb.append(entry.getKey()).append("=").append(value)
                    .append("&");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String send(String url, String requestStr, String appId, String appKey) throws PayException {
        // 请求的URI
        URI uri = URI.create(url);
        try {
            // 构建GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", openBodySig(requestStr, appId, appKey))
                    .header("Content-Type", "application/json")
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
        } catch (Exception e) {
            throw new PayException("An error occurred while sending the HTTP request: " + e.getMessage(), e);
        }
    }

    public static String getCurrentTime(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter);
    }

    /**
     * 获得一个指定长度的随机的数字
     *
     * @param length     字符串的长度
     * @return 随机字符串
     */
    public static String randomNumber(int length) {
        String baseString = "0123456789";
        if (length < 1) {
            length = 1;
        }

        final StringBuilder sb = new StringBuilder(length);
        int baseLength = baseString.length();
        for (int i = 0; i < length; i++) {
            int number = ThreadLocalRandom.current().nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }
}
