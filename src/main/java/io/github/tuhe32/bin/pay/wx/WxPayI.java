package io.github.tuhe32.bin.pay.wx;

import io.github.tuhe32.bin.pay.common.exception.PayException;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 刘斌
 * @date 2024/4/30 12:35
 */
public interface WxPayI {

    /**
     * 扫码支付
     *
     * @param sn            订单号
     * @param amount        金额
     * @param subject       订单标题
     * @return              二维码链接
     * @throws PayException 支付异常
     */
    String nativePay(String sn, BigDecimal amount, String subject) throws PayException;

    /**
     * 扫码支付
     *
     * @param sn            订单号
     * @param amount        金额
     * @param subject       订单标题
     * @param expireTime    过期时间
     * @return              二维码链接
     * @throws PayException 支付异常
     */
    String nativePayDate(String sn, BigDecimal amount, String subject, Date expireTime) throws PayException;

    /**
     * 扫码支付
     *
     * @param sn            订单号
     * @param amount        金额
     * @param subject       订单标题
     * @param expireTime    过期时间
     * @return              二维码链接
     * @throws PayException 支付异常
     */
    String nativePay(String sn, BigDecimal amount, String subject, LocalDateTime expireTime) throws PayException;

    /**
     * 扫码支付
     *
     * @param request       请求参数
     * @return              二维码链接
     * @throws PayException 支付异常
     */
    String nativePay(WxPayUnifiedOrderV3Request request) throws PayException;

    /**
     * JSAPI支付
     *
     * @param sn            订单号
     * @param amount        金额
     * @param subject       订单标题
     * @param openId        用户openId
     * @return JsapiResult  JSAPI拉起支付需要的参数
     * @throws PayException 支付异常
     */
    WxPayUnifiedOrderV3Result.JsapiResult jsapiPay(String sn, BigDecimal amount, String subject, String openId) throws PayException;

    /**
     * JSAPI支付
     *
     * @param sn            订单号
     * @param amount        金额
     * @param subject       订单标题
     * @param openId        用户openId
     * @param expireTime    过期时间
     * @return JsapiResult  JSAPI拉起支付需要的参数
     * @throws PayException 支付异常
     */
    WxPayUnifiedOrderV3Result.JsapiResult jsapiPayDate(String sn, BigDecimal amount, String subject, String openId, Date expireTime) throws PayException;

    /**
     * JSAPI支付
     *
     * @param sn            订单号
     * @param amount        金额
     * @param subject       订单标题
     * @param openId        用户openId
     * @param expireTime    过期时间
     * @return JsapiResult  JSAPI拉起支付需要的参数
     * @throws PayException 支付异常
     */
    WxPayUnifiedOrderV3Result.JsapiResult jsapiPay(String sn, BigDecimal amount, String subject, String openId, LocalDateTime expireTime) throws PayException;

    /**
     * JSAPI支付
     *
     * @param request       请求参数
     * @return JsapiResult  JSAPI拉起支付需要的参数
     * @throws PayException 支付异常
     */
    WxPayUnifiedOrderV3Result.JsapiResult jsapiPay(WxPayUnifiedOrderV3Request request) throws PayException;

    /**
     * H5支付
     *
     * @param sn            订单号
     * @param amount        金额
     * @param subject       订单标题
     * @param ipAddress     用户IP
     * @param expireTime    过期时间
     * @return h5_url       访问该URL拉起微信客户端,有效期为5分钟
     * @throws PayException 支付异常
     */
    String h5Pay(String sn, BigDecimal amount, String subject, String ipAddress, LocalDateTime expireTime) throws PayException;

    /**
     * H5支付
     *
     * @param sn            订单号
     * @param amount        金额
     * @param subject       订单标题
     * @param ipAddress     用户IP
     * @return h5_url       访问该URL拉起微信客户端,有效期为5分钟
     * @throws PayException 支付异常
     */
    String h5Pay(String sn, BigDecimal amount, String subject, String ipAddress) throws PayException;

    /**
     * H5支付
     *
     * @param sn            订单号
     * @param amount        金额
     * @param subject       订单标题
     * @param ipAddress     用户IP
     * @param expireTime    过期时间
     * @return h5_url       访问该URL拉起微信客户端,有效期为5分钟
     * @throws PayException 支付异常
     */
    String h5PayDate(String sn, BigDecimal amount, String subject, String ipAddress, Date expireTime) throws PayException;

    /**
     * H5支付
     *
     * @param request       请求参数
     * @return h5_url       访问该URL拉起微信客户端,有效期为5分钟
     * @throws PayException 支付异常
     */
    String h5Pay(WxPayUnifiedOrderV3Request request) throws PayException;

    /**
     * APP支付
     *
     * @param sn            商户订单号
     * @param amount        金额
     * @param subject       商品标题
     * @return              APP拉起支付需要的参数
     * @throws PayException 支付异常
     */
    WxPayUnifiedOrderV3Result.AppResult appPay(String sn, BigDecimal amount, String subject) throws PayException;

    /**
     * APP支付（带过期时间Date类型）
     *
     * @param sn            商户订单号
     * @param amount        金额
     * @param subject       商品标题
     * @param expireTime    过期时间
     * @return              APP拉起支付需要的参数
     * @throws PayException 支付异常
     */
    WxPayUnifiedOrderV3Result.AppResult appPayDate(String sn, BigDecimal amount, String subject, Date expireTime) throws PayException;

    /**
     * APP支付（带过期时间LocalDateTime类型）
     *
     * @param sn            商户订单号
     * @param amount        金额
     * @param subject       商品标题
     * @param expireTime    过期时间
     * @return              APP拉起支付需要的参数
     * @throws PayException 支付异常
     */
    WxPayUnifiedOrderV3Result.AppResult appPay(String sn, BigDecimal amount, String subject, LocalDateTime expireTime) throws PayException;

    /**
     * APP支付（通过请求参数）
     *
     * @param request       请求参数
     * @return              APP拉起支付需要的参数
     * @throws PayException 支付异常
     */
    WxPayUnifiedOrderV3Result.AppResult appPay(WxPayUnifiedOrderV3Request request) throws PayException;

    /**
     * 查询订单
     *
     * @param sn                        商户订单号
     * @return WxPayOrderQueryV3Result  订单信息
     * @throws PayException             支付异常
     */
    WxPayOrderQueryV3Result query(String sn) throws PayException;

    /**
     * 查询订单
     *
     * @param sn                        商户订单号
     * @param transactionId             微信订单号
     * @return WxPayOrderQueryV3Result  订单信息
     * @throws PayException             支付异常
     */
    WxPayOrderQueryV3Result query(String sn, String transactionId) throws PayException;

    /**
     * 查询订单
     *
     * @param request                   请求参数
     * @return WxPayOrderQueryV3Result  订单信息
     * @throws PayException             支付异常
     */
    WxPayOrderQueryV3Result query(WxPayOrderQueryV3Request request) throws PayException;

    /**
     * 申请退款
     *
     * @param sn                      商户订单号
     * @param amount                  退款金额
     * @param outRequestNo            退款单号
     * @return WxPayRefundV3Result    退款信息
     * @throws PayException           支付异常
     */
    WxPayRefundV3Result refund(String sn, BigDecimal amount, String outRequestNo) throws PayException;

    /**
     * 申请退款
     *
     * @param sn                      商户订单号
     * @param transactionId           微信订单号
     * @param amount                  退款金额
     * @param outRequestNo            退款单号
     * @return WxPayRefundV3Result    退款信息
     * @throws PayException           支付异常
     */
    WxPayRefundV3Result refund(String sn, String transactionId, BigDecimal amount, String outRequestNo) throws PayException;

    /**
     * 申请退款
     *
     * @param request                 请求参数
     * @return WxPayRefundV3Result    退款信息
     * @throws PayException           支付异常
     */
    WxPayRefundV3Result refund(WxPayRefundV3Request request) throws PayException;

    /**
     * 微信支付回调
     *
     * @param notifyData 回调数据
     * @return WxPayNotifyV3Result.DecryptNotifyResult    回调信息
     * @throws PayException 支付异常
     */
    WxPayNotifyV3Result.DecryptNotifyResult notify(String notifyData) throws PayException;

    /**
     * 微信支付回调
     *
     * @param notifyData 回调数据
     * @param header     回调头信息
     * @return WxPayNotifyV3Result.DecryptNotifyResult    回调信息
     * @throws PayException 支付异常
     */
    WxPayNotifyV3Result.DecryptNotifyResult notify(String notifyData, SignatureHeader header) throws PayException;
}
