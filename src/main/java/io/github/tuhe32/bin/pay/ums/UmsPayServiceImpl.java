package io.github.tuhe32.bin.pay.ums;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.github.tuhe32.bin.pay.common.BasePayServiceImpl;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import io.github.tuhe32.bin.pay.common.utils.PayUtils;
import io.github.tuhe32.bin.pay.ums.config.UmsPayConfig;
import io.github.tuhe32.bin.pay.ums.config.UmsPayConfigHolder;
import io.github.tuhe32.bin.pay.ums.domain.*;
import io.github.tuhe32.bin.pay.ums.util.UmsSignUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.github.tuhe32.bin.pay.ums.constants.UmsPayConstants.*;

/**
 * @author 刘斌
 * @date 2024/5/11 15:39
 */
@Slf4j
@SuppressWarnings({"spellcheck", "Duplicates"})
public class UmsPayServiceImpl extends BasePayServiceImpl implements UmsPayService {

    private static final Gson GSON = new GsonBuilder().create();

    public UmsPayServiceImpl() {
        super.setConfigHolder(new UmsPayConfigHolder());
    }

    /**
     * 小程序支付
     *
     * @param request 小程序支付请求
     * @return UmsPayMiniResponse
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public UmsPayMiniResponse miniPay(UmsPayMiniRequest request) throws PayException {
        request.setInstMid("MINIDEFAULT");
        request.setTradeType("MINI");
        String serverUrl = "";
        if (PAY_TYPE_WXPAY.equals(request.getPayType())) {
            serverUrl = MINI_WEIXIN_ORDER;
        } else if (PAY_TYPE_ALIPAY.equals(request.getPayType())) {
            serverUrl = MINI_ALI_ORDER;
        } else if (PAY_TYPE_UACPAY.equals(request.getPayType())) {
            serverUrl = MINI_UAC_ORDER;
        }
        Map<String, String> responseMap = this.nativePay(request, serverUrl, "小程序下单");
        return UmsPayMiniResponse.of(responseMap);
    }

    /**
     * APP支付
     *
     * @param request APP支付请求
     * @return UmsPayAppResponse
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public UmsPayAppResponse appPay(UmsPayAppRequest request) throws PayException {
        request.setInstMid("APPDEFAULT");
        request.setTradeType("APP");
        String serverUrl = "";
        if (PAY_TYPE_WXPAY.equals(request.getPayType())) {
            serverUrl = APP_WEIXIN_ORDER;
        } else if (PAY_TYPE_ALIPAY.equals(request.getPayType())) {
            serverUrl = APP_ALI_ORDER;
        } else if (PAY_TYPE_UACPAY.equals(request.getPayType())) {
            serverUrl = APP_UAC_ORDER;
        }
        Map<String, String> responseMap = this.nativePay(request, serverUrl, "APP下单");
        return UmsPayAppResponse.of(responseMap);
    }

    /**
     * H5支付
     * 由浏览器直接跳转，不需要应答报文
     * 生成支付链接的形式，用户访问这个链接，跳转到一个页面上进行支付（跳转支付）
     *
     * @param request H5支付请求
     * @return 支付链接
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public String h5Pay(UmsPayMiniRequest request) throws PayException {
        request.setInstMid("YUEDANDEFAULT");
        UmsPayConfig config = (UmsPayConfig) this.getConfig();
        if (StringUtils.isBlank(request.getMerOrderId())) {
            request.setMerOrderId(getMerOrderId(config.getSysCodePrefix()));
        }
        request.setRequestTimestamp(UmsSignUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        if (PAY_TYPE_WXPAY.equals(request.getPayType())) {
            if (StringUtils.isBlank(request.getSubAppId())) {
                request.setSubAppId(config.getSubAppId());
            }
        }
        if (config.getDivisionFlag() != null && config.getDivisionFlag()) {
            request.setPlatformAmount(request.getPlatformAmount() == null ? 0 : request.getPlatformAmount());
            if (StringUtils.isBlank(request.getSubOrders())) {
                JsonArray subOrders = new JsonArray();
                JsonObject order1 = new JsonObject();
                order1.addProperty("mid", config.getCusId());
                order1.addProperty("totalAmount", request.getTotalAmount() - request.getPlatformAmount());
                order1.addProperty("merOrderId", request.getMerOrderId());
                subOrders.add(order1);
                request.setSubOrders(GSON.toJson(subOrders));
            }
        }
        this.buildBaseParams(request, config);
        String json = GSON.toJson(request);
        Map<String, String> map = new HashMap<>(10);
        try {
            String timestamp = UmsSignUtils.getCurrentTime("yyyyMMddHHmmss");
            String nonce = UUID.randomUUID().toString().replace("-", "");
            byte[] data = json.getBytes(StandardCharsets.UTF_8);

            InputStream is = new ByteArrayInputStream(data);
            String bodyDigest = DigestUtils.sha256Hex(is);
            String str1C = config.getAppId() + timestamp + nonce + bodyDigest;
            byte[] localSignature = UmsSignUtils.hmacSHA256(str1C.getBytes(), config.getAppKey().getBytes());
            // Signature
            String localSignatureStr = Base64.encodeBase64String(localSignature);

            map.put("authorization", "OPEN-FORM-PARAM");
            map.put("appId", config.getAppId());
            map.put("timestamp", timestamp);
            map.put("nonce", nonce);
            map.put("content", URLEncoder.encode(json, StandardCharsets.UTF_8));
            map.put("signature", localSignatureStr);
        } catch (Exception e) {
            throw new PayException("签名异常", e);
        }
        return config.getDomainUrl() + H5_WEXIN_ORDER + "?" + UmsSignUtils.buildUrlStr(map);
    }

    /**
     * JS支付
     * 返回报文中的参数，调用微信的JSAPI支付方式，拉起支付
     *
     * @param request JS支付请求
     * @return UmsPayJsResponse
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public UmsPayJsResponse jsPay(UmsPayMiniRequest request) throws PayException {
        request.setInstMid("YUEDANDEFAULT");
        request.setTradeType("JSAPI");
        String serverUrl = "";
        if (PAY_TYPE_WXPAY.equals(request.getPayType())) {
            serverUrl = MINI_WEIXIN_ORDER;
        } else if (PAY_TYPE_ALIPAY.equals(request.getPayType())) {
            serverUrl = MINI_ALI_ORDER;
        } else if (PAY_TYPE_UACPAY.equals(request.getPayType())) {
            serverUrl = JS_ACP_ORDER;
        }
        Map<String, String> responseMap = this.nativePay(request, serverUrl, "JS支付");
        return UmsPayJsResponse.of(responseMap);
    }

    public Map<String, String> nativePay(BaseUmsNativePayRequest request, String serverUrl, String operate) throws PayException {
        UmsPayConfig config = (UmsPayConfig) this.getConfig();
        if (StringUtils.isBlank(request.getMerOrderId())) {
            request.setMerOrderId(getMerOrderId(config.getSysCodePrefix()));
        }
        request.setRequestTimestamp(UmsSignUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        if (PAY_TYPE_WXPAY.equals(request.getPayType())) {
            if (StringUtils.isBlank(request.getSubAppId())) {
                request.setSubAppId(config.getSubAppId());
            }
        }
        if (config.getDivisionFlag() != null && config.getDivisionFlag()) {
            request.setPlatformAmount(request.getPlatformAmount() == null ? 0 : request.getPlatformAmount());
            if (StringUtils.isBlank(request.getSubOrders())) {
                JsonArray subOrders = new JsonArray();
                JsonObject order1 = new JsonObject();
                order1.addProperty("mid", config.getCusId());
                order1.addProperty("totalAmount", request.getTotalAmount() - request.getPlatformAmount());
                order1.addProperty("merOrderId", request.getMerOrderId());
                subOrders.add(order1);
                request.setSubOrders(GSON.toJson(subOrders));
            }
        }
        this.buildBaseParams(request, config);
        return this.postUms(serverUrl, GSON.toJson(request), config, operate);
    }

    /**
     * 扫码支付
     *
     * @param request 扫码支付请求
     * @return UmsPayScanQrPayResponse
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public UmsPayScanQrPayResponse scanQrPay(UmsPayScanQrPayRequest request) throws PayException {
        UmsPayConfig config = (UmsPayConfig) this.getConfig();
        if (StringUtils.isBlank(request.getMerchantOrderId())) {
            request.setMerchantOrderId(getMerOrderId(config.getSysCodePrefix()));
        }
        request.setTransactionCurrencyCode("156");
        request.setPayMode("CODE_SCAN");
        request.setDeviceType("11");
        request.setSerialNum("167417524P");
        if (config.getDivisionFlag() != null && config.getDivisionFlag()) {
            request.setMerchantCode(config.getParentMid());
        } else {
            request.setMerchantCode(config.getCusId());
        }
        request.setTerminalCode(config.getTid());
        this.clearConfigHolder();
        Map<String, String> responseMap = this.postUms(SCAN_QR_ORDER, GSON.toJson(request), config, "POS扫码下单");
        return UmsPayScanQrPayResponse.of(responseMap);
    }

    /**
     * 小程序支付查询
     *
     * @param request 小程序支付查询请求
     * @return UmsPayQueryResponse
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public UmsPayQueryResponse miniQuery(UmsPayQueryRequest request) throws PayException {
        request.setInstMid("QRPAYDEFAULT");
        return this.query(request, MINI_QUERY);
    }

    /**
     * JS支付查询
     *
     * @param request JS支付查询请求
     * @return UmsPayQueryResponse
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public UmsPayQueryResponse jsQuery(UmsPayQueryRequest request) throws PayException {
        request.setInstMid("YUEDANDEFAULT");
        return this.query(request, MINI_QUERY);
    }

    /**
     * APP支付查询
     *
     * @param request APP支付查询请求
     * @return UmsPayQueryResponse
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public UmsPayQueryResponse appQuery(UmsPayQueryRequest request) throws PayException {
        request.setInstMid("APPDEFAULT");
        String serverUrl = "";
        if (PAY_TYPE_WXPAY.equals(request.getPayType())) {
            serverUrl = APP_WEIXIN_QUERY;
        } else if (PAY_TYPE_ALIPAY.equals(request.getPayType())) {
            serverUrl = APP_ALI_QUERY;
        }
        return this.query(request, serverUrl);
    }

    public UmsPayQueryResponse query(UmsPayQueryRequest request, String serverUrl) throws PayException {
        UmsPayConfig config = (UmsPayConfig) this.getConfig();
        request.setRequestTimestamp(UmsSignUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        this.buildMidParams(request, config);
        Map<String, String> responseMap = this.postUms(serverUrl, GSON.toJson(request), config, "交易查询");
        if (!STATUS_SUCCESS.equals(responseMap.get("status"))) {
            log.error("银商查询交易支付失败【请求参数】：{}", GSON.toJson(responseMap));
            throw new PayException(responseMap.get("status"), responseMap.get("errMsg"));
        }
        return UmsPayQueryResponse.of(responseMap);
    }

    /**
     * 小程序或H5,JS退款
     *
     * @param request 小程序或H5,JS退款退款请求
     * @return UmsPayRefundResponse
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public UmsPayRefundResponse miniRefund(UmsPayRefundRequest request) throws PayException {
        request.setInstMid("YUEDANDEFAULT");
        return this.refund(request, "小程序退款");
    }

    /**
     * APP退款
     *
     * @param request APP退款请求
     * @return UmsPayRefundResponse
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public UmsPayRefundResponse appRefund(UmsPayRefundRequest request) throws PayException {
        request.setInstMid("APPDEFAULT");
        return this.refund(request, "APP退款");
    }

    public UmsPayRefundResponse refund(UmsPayRefundRequest request, String operate) throws PayException {
        UmsPayConfig config = (UmsPayConfig) this.getConfig();
        request.setRequestTimestamp(UmsSignUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        if (config.getDivisionFlag() != null && config.getDivisionFlag()) {
            request.setPlatformAmount(request.getPlatformAmount() == null ? 0 : request.getPlatformAmount());
            if (StringUtils.isBlank(request.getSubOrders())) {
                JsonArray subOrders = new JsonArray();
                JsonObject order1 = new JsonObject();
                order1.addProperty("mid", config.getCusId());
                order1.addProperty("totalAmount", request.getRefundAmount() - request.getPlatformAmount());
                order1.addProperty("merOrderId", request.getMerOrderId());
                subOrders.add(order1);
                request.setSubOrders(GSON.toJson(subOrders));
            }
        }
        this.buildMidParams(request, config);
        Map<String, String> responseMap = this.postUms(REFUND_ORDER, GSON.toJson(request), config, operate);
        if (!CODE_SUCCESS.equals(responseMap.get("refundStatus"))) {
            log.error("银商退款失败【请求参数】：{}", GSON.toJson(responseMap));
            throw new PayException(responseMap.get("refundStatus"), responseMap.get("errMsg"));
        }
        return UmsPayRefundResponse.of(responseMap);
    }

    /**
     * 扫码支付退款
     *
     * @param request 扫码支付退款请求
     * @return UmsPayScanQrRefundResponse
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public UmsPayScanQrRefundResponse scanQrRefund(UmsPayScanQrRefundRequest request) throws PayException {
        UmsPayConfig config = (UmsPayConfig) this.getConfig();
        if (config.getDivisionFlag() != null && config.getDivisionFlag()) {
            request.setMerchantCode(config.getParentMid());
        } else {
            request.setMerchantCode(config.getCusId());
        }
        request.setTerminalCode(config.getTid());
        this.clearConfigHolder();
        Map<String, String> responseMap = this.postUms(SCAN_QR_REFUND_ORDER, GSON.toJson(request), config, "POS退款");
        return UmsPayScanQrRefundResponse.of(responseMap);
    }

    /**
     * 支付或退款回调
     *
     * @param request 扫码支付回调请求
     * @return UmsPayNotify
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public UmsPayNotify notify(HttpServletRequest request) throws PayException {
        Map<String, String> requestMap = PayUtils.getRequestMap(request);
        this.switchoverTo(requestMap.get("mid"));
        UmsPayConfig config = (UmsPayConfig) this.getConfig();
        UmsSignUtils.validSign(requestMap, config.getMd5Key());
        this.clearConfigHolder();
        if (!STATUS_SUCCESS.equals(requestMap.get("status")) || !STATUS_CLOSED.equals(requestMap.get("status"))) {
            log.error("回调显示交易失败【请求参数】：{}", GSON.toJson(requestMap));
            throw new PayException(requestMap.get("status"), requestMap.get("errMsg"));
        }
        log.info("回调显示交易成功【请求参数】：{}", GSON.toJson(requestMap));
        return UmsPayNotify.of(requestMap);
    }

    public void buildMidParams(BaseUmsPayRequest request, UmsPayConfig config) {
        if (config.getDivisionFlag() != null && config.getDivisionFlag()) {
            request.setMid(config.getParentMid());
        } else {
            request.setMid(config.getCusId());
        }
        request.setTid(config.getTid());
        this.clearConfigHolder();
    }

    public void buildBaseParams(BaseUmsPayRequest request, UmsPayConfig config) {
        this.buildMidParams(request, config);
        request.setSignType(SIGN_TYPE);
        request.setNotifyUrl(config.getNotifyUrl());
    }

    private Map<String, String> postUms(String url, String requestStr, UmsPayConfig config, String operate) throws PayException {
        try {
            String respStr = UmsSignUtils.send(config.getDomainUrl() + url, requestStr, config.getAppId(), config.getAppKey());
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> map = GSON.fromJson(respStr, type);
            if (CODE_SUCCESS.equals(map.get("errCode"))) {
                log.info("银商{}调用成功【请求数据】：{}\n【响应数据】：{}", operate, requestStr, respStr);
                return map;
            } else {
                throw new PayException(map.get("errCode"), map.get("errMsg") == null ? map.get("errInfo") : map.get("errMsg"));
            }
        } catch (Exception e) {
            log.error("银商{}调用失败【请求数据】：{}\n【原因】：{}", operate, requestStr, e.getMessage());
            throw (e instanceof PayException) ? (PayException) e : new PayException(e.getMessage(), e);
        }
    }

    private String getMerOrderId(String prefix) {
        String time = UmsSignUtils.getCurrentTime("yyyyMMddHHmmssSSS");
        String result = UmsSignUtils.randomNumber(7);
        return prefix + time + result;
    }

}
