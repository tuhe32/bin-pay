package io.github.tuhe32.bin.pay.wx;

import com.github.binarywang.wxpay.config.WxPayConfig;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/4/27 17:29
 */
@Slf4j
@RequiredArgsConstructor
public class WxPay implements WxPayI {

    private final WxPayService wxPayService;

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public WxPayConfig getConfig() {
        return wxPayService.getConfig();
    }

    @Override
    public void setConfig(WxPayConfig config) {
        wxPayService.setConfig(config);
    }

    @Override
    public void addConfig(String mchId, WxPayConfig wxPayConfig) {
        wxPayService.addConfig(mchId, wxPayConfig);
    }

    @Override
    public void removeConfig(String mchId) {
        wxPayService.removeConfig(mchId);
    }

    @Override
    public void setMultiConfig(Map<String, WxPayConfig> wxPayConfigMap) {
        wxPayService.setMultiConfig(wxPayConfigMap);
    }

    @Override
    public void setMultiConfig(Map<String, WxPayConfig> wxPayConfigMap, String defaultMchId) {
        wxPayService.setMultiConfig(wxPayConfigMap, defaultMchId);
    }

    @Override
    public boolean switchover(String mchId) {
        return wxPayService.switchover(mchId);
    }

    @Override
    public boolean switchoverTo(String mchId) throws PayException {
        try {
            wxPayService.switchoverTo(mchId);
            return true;
        } catch (Exception e) {
            throw new PayException(e.getMessage());
        }
    }

    @Override
    public String nativePay(String sn, BigDecimal amount, String subject) throws PayException {
        return this.nativePay(sn, amount, subject, null);
    }

    @Override
    public String nativePayDate(String sn, BigDecimal amount, String subject, Date expireTime) throws PayException {
        return this.nativePay(sn, amount, subject, this.dateToLocalDateTime(expireTime));
    }

    @Override
    public String nativePay(String sn, BigDecimal amount, String subject, LocalDateTime expireTime) throws PayException {
        return this.nativePay(this.buildNormalRequest(sn, amount, subject, expireTime));
    }

    /**
     * 扫码支付
     *
     * @param request 请求参数
     * @return 二维码链接
     * @throws PayException 支付异常
     */
    @Override
    public String nativePay(WxPayUnifiedOrderV3Request request) throws PayException {
        return this.postPayV3(TradeTypeEnum.NATIVE, request);
    }

    @Override
    public WxPayUnifiedOrderV3Result.JsapiResult jsapiPay(String sn, BigDecimal amount, String subject, String openId) throws PayException {
        return this.jsapiPay(sn, amount, subject, openId, null);
    }

    @Override
    public WxPayUnifiedOrderV3Result.JsapiResult jsapiPayDate(String sn, BigDecimal amount, String subject, String openId, Date expireTime) throws PayException {
        return this.jsapiPay(sn, amount, subject, openId, this.dateToLocalDateTime(expireTime));
    }

    @Override
    public WxPayUnifiedOrderV3Result.JsapiResult jsapiPay(String sn, BigDecimal amount, String subject, String openId, LocalDateTime expireTime) throws PayException {
        WxPayUnifiedOrderV3Request request = this.buildNormalRequest(sn, amount, subject, expireTime);
        request.setPayer(new WxPayUnifiedOrderV3Request.Payer().setOpenid(openId));
        return this.jsapiPay(request);
    }

    /**
     * JSAPI支付
     *
     * @param request 请求参数
     * @return JsapiResult JSAPI拉起支付需要的参数
     * @throws PayException 支付异常
     */
    @Override
    public WxPayUnifiedOrderV3Result.JsapiResult jsapiPay(WxPayUnifiedOrderV3Request request) throws PayException {
        return this.postPayV3(TradeTypeEnum.JSAPI, request);
    }

    @Override
    public String h5Pay(String sn, BigDecimal amount, String subject, String ipAddress) throws PayException {
        return this.h5Pay(sn, amount, subject, ipAddress, null);
    }

    @Override
    public String h5PayDate(String sn, BigDecimal amount, String subject, String ipAddress, Date expireTime) throws PayException {
        return this.h5Pay(sn, amount, subject, ipAddress, this.dateToLocalDateTime(expireTime));
    }

    @Override
    public String h5Pay(String sn, BigDecimal amount, String subject, String ipAddress, LocalDateTime expireTime) throws PayException {
        WxPayUnifiedOrderV3Request request = this.buildNormalRequest(sn, amount, subject, expireTime);
        WxPayUnifiedOrderV3Request.SceneInfo sceneInfo = new WxPayUnifiedOrderV3Request.SceneInfo();
        // 场景类型示例值：iOS, Android, Wap
        sceneInfo.setPayerClientIp(ipAddress).setH5Info(new WxPayUnifiedOrderV3Request.H5Info().setType("Wap"));
        request.setSceneInfo(sceneInfo);
        return this.h5Pay(request);
    }

    /**
     * H5支付
     *
     * @param request 请求参数
     * @return h5_url       访问该URL拉起微信客户端,有效期为5分钟
     * @throws PayException 支付异常
     */
    @Override
    public String h5Pay(WxPayUnifiedOrderV3Request request) throws PayException {
        return this.postPayV3(TradeTypeEnum.H5, request);
    }

    @Override
    public WxPayUnifiedOrderV3Result.AppResult appPay(String sn, BigDecimal amount, String subject) throws PayException {
        return this.appPay(sn, amount, subject, null);
    }

    @Override
    public WxPayUnifiedOrderV3Result.AppResult appPayDate(String sn, BigDecimal amount, String subject, Date expireTime) throws PayException {
        return this.appPay(sn, amount, subject, this.dateToLocalDateTime(expireTime));
    }

    @Override
    public WxPayUnifiedOrderV3Result.AppResult appPay(String sn, BigDecimal amount, String subject, LocalDateTime expireTime) throws PayException {
        return this.appPay(this.buildNormalRequest(sn, amount, subject, expireTime));
    }

    /**
     * APP支付
     *
     * @param request 请求参数
     * @return AppResult    APP拉起支付需要的参数
     * @throws PayException 支付异常
     */
    @Override
    public WxPayUnifiedOrderV3Result.AppResult appPay(WxPayUnifiedOrderV3Request request) throws PayException {
        return this.postPayV3(TradeTypeEnum.APP, request);
    }

    /**
     * 查询订单
     *
     * @param sn 商户订单号
     * @return WxPayOrderQueryV3Result    订单信息
     * @throws PayException 支付异常
     */
    @Override
    public WxPayOrderQueryV3Result query(String sn) throws PayException {
        return this.query(sn, null);
    }

    /**
     * 查询订单
     *
     * @param sn            商户订单号
     * @param transactionId 微信订单号
     * @return WxPayOrderQueryV3Result    订单信息
     * @throws PayException 支付异常
     */
    @Override
    public WxPayOrderQueryV3Result query(String sn, String transactionId) throws PayException {
        WxPayOrderQueryV3Request request = new WxPayOrderQueryV3Request();
        request.setOutTradeNo(StringUtils.trimToNull(sn));
        request.setTransactionId(StringUtils.trimToNull(transactionId));
        return this.query(request);
    }

    /**
     * 查询订单
     *
     * @param request 请求参数
     * @return WxPayOrderQueryV3Result    订单信息
     * @throws PayException 支付异常
     */
    @Override
    public WxPayOrderQueryV3Result query(WxPayOrderQueryV3Request request) throws PayException {
        try {
            WxPayOrderQueryV3Result result = wxPayService.queryOrderV3(request);
            if ("SUCCESS".equals(result.getTradeState())) {
                log.info("微信查询成功【请求数据】：{}\n【响应数据】：{}", GSON.toJson(request), GSON.toJson(result));
                return result;
            } else {
                throw new PayException(result.getTradeState(), GSON.toJson(result));
            }
        } catch (Exception e) {
            log.info("微信查询失败【请求数据】：{}\n【原因】：{}", GSON.toJson(request), e.getMessage());
            throw (e instanceof PayException) ? (PayException) e : new PayException(e.getMessage(), e);
        }
    }

    /**
     * 申请退款
     *
     * @param sn           商户订单号
     * @param amount       退款金额
     * @param outRequestNo 退款单号
     * @return WxPayRefundV3Result    退款信息
     * @throws PayException 支付异常
     */
    @Override
    public WxPayRefundV3Result refund(String sn, BigDecimal amount, String outRequestNo) throws PayException {
        return this.refund(sn, null, amount, outRequestNo);
    }

    /**
     * 申请退款
     *
     * @param sn            商户订单号
     * @param transactionId 微信订单号
     * @param amount        退款金额
     * @param outRequestNo  退款单号
     * @return WxPayRefundV3Result    退款信息
     * @throws PayException 支付异常
     */
    @Override
    public WxPayRefundV3Result refund(String sn, String transactionId, BigDecimal amount, String outRequestNo) throws PayException {
        Integer fen = BaseWxPayRequest.yuan2Fen(amount);
        WxPayRefundV3Request request = new WxPayRefundV3Request();
        request.setTransactionId(transactionId);
        request.setOutTradeNo(sn);
        request.setOutRefundNo(outRequestNo);
        request.setAmount(new WxPayRefundV3Request.Amount()
                .setCurrency("CNY")
                .setTotal(fen)
                .setRefund(fen));
        return this.refund(request);
    }

    /**
     * 申请退款
     *
     * @param request 请求参数
     * @return WxPayRefundV3Result    退款信息
     * @throws PayException 支付异常
     */
    @Override
    public WxPayRefundV3Result refund(WxPayRefundV3Request request) throws PayException {
        try {
            WxPayRefundV3Result result = wxPayService.refundV3(request);
            if ("SUCCESS".equals(result.getStatus())) {
                log.info("微信退款成功【请求数据】：{}\n【响应数据】：{}", GSON.toJson(request), GSON.toJson(result));
                return result;
            } else {
                throw new PayException(result.getStatus(), GSON.toJson(result));
            }
        } catch (Exception e) {
            log.info("微信退款失败【请求数据】：{}\n【原因】：{}", GSON.toJson(request), e.getMessage());
            throw (e instanceof PayException) ? (PayException) e : new PayException(e.getMessage(), e);
        }
    }

    @Override
    public WxPayNotifyV3Result.DecryptNotifyResult notify(String notifyData) throws PayException {
        return this.notify(notifyData, null);
    }

    /**
     * 订单回调通知
     */
    @Override
    public WxPayNotifyV3Result.DecryptNotifyResult notify(String notifyData, SignatureHeader header) throws PayException {
        try {
            WxPayNotifyV3Result notifyResult = wxPayService.parseOrderNotifyV3Result(notifyData, header);
            WxPayNotifyV3Result.DecryptNotifyResult result = notifyResult.getResult();
            if ("SUCCESS".equals(result.getTradeState())) {
                log.info("微信回调成功【响应数据】：{}", GSON.toJson(result));
                return result;
            } else {
                throw new PayException(result.getTradeState(), GSON.toJson(result));
            }
        } catch (Exception e) {
            log.info("微信回调失败【原因】：{}", e.getMessage());
            throw (e instanceof PayException) ? (PayException) e : new PayException(e.getMessage(), e);
        }
    }

    private WxPayUnifiedOrderV3Request buildNormalRequest(String sn, BigDecimal amount, String subject, LocalDateTime expireTime) {
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request()
                .setOutTradeNo(sn)
                .setAmount(new WxPayUnifiedOrderV3Request.Amount()
                        .setCurrency("CNY")
                        .setTotal(BaseWxPayRequest.yuan2Fen(amount)))
                .setDescription(subject);
        if (expireTime != null) {
            request.setTimeExpire(String.format("%s+08:00", dateToStr(expireTime)));
        }
        return request;
    }

    private <T> T postPayV3(TradeTypeEnum tradeType, WxPayUnifiedOrderV3Request request) throws PayException {
        try {
            T response = wxPayService.createOrderV3(tradeType, request);
            log.info("微信调用成功【请求数据】：{}\n【响应数据】：{}", GSON.toJson(request), response instanceof String ? response : GSON.toJson(response));
            return response;
        } catch (Exception e) {
            log.error("微信调用失败【请求数据】：{}\n【原因】：{}", GSON.toJson(request), e.getMessage());
            throw new PayException(e.getMessage(), e);
        }
    }

    private LocalDateTime dateToLocalDateTime(Date date) {
        // 实现从Date转换成LocalDateTime
        Instant instant = date.toInstant();
        // 转换时区到系统默认时区（如果需要特定时区，可以指定ZoneId）
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private String dateToStr(LocalDateTime localDateTime) {
        // 创建一个日期时间格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        // 格式化为字符串
        String formattedExpireTime = localDateTime.format(formatter);
        return String.format("%s+08:00", formattedExpireTime);
    }
}
