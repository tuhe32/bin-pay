package io.github.tuhe32.bin.pay.ali;

import com.alipay.api.domain.*;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import io.github.tuhe32.bin.pay.ali.config.AliPayConfig;
import io.github.tuhe32.bin.pay.ali.domain.AliPayNotify;
import io.github.tuhe32.bin.pay.ali.domain.AliPayQuery;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/4/27 09:35
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface AliPayService {

    /**
     * 获取配置
     * @return 可能为null的AliPayConfig对象
     */
    AliPayConfig getConfig();

    /**
     * 设置配置
     * @param config 配置对象
     */
    void setConfig(AliPayConfig config);

    /**
     * 添加配置
     * @param appId 应用ID
     * @param aliPayConfig 配置对象
     */
    void addConfig(String appId, AliPayConfig aliPayConfig);

    /**
     * 移除配置
     * @param appId 应用ID
     */
    void removeConfig(String appId);

    /**
     * 设置多配置
     * @param aliPayConfigMap 多个配置的映射表
     */
    void setMultiConfig(Map<String, AliPayConfig> aliPayConfigMap);

    /**
     * 设置多配置并指定默认应用ID
     * @param aliPayConfigMap 多个配置的映射表
     * @param defaultAppId 默认应用ID
     */
    void setMultiConfig(Map<String, AliPayConfig> aliPayConfigMap, String defaultAppId);

    /**
     * 切换至指定应用ID的配置
     * @param appId 应用ID
     * @return 是否切换成功
     */
    boolean switchover(String appId);

    /**
     * 切换至指定应用ID的配置并返回当前对象
     * @param appId 应用ID
     * @return 当前AliPayService实例
     * @throws PayException 如果找不到指定应用ID的配置
     */
    boolean switchoverTo(String appId) throws PayException;

    /**
     * 电脑网站支付
     * @param sn 平台订单号
     * @param amount 用户支付金额(单位: 元)
     * @param subject 商品标题
     * @return 支付页面HTML形式的响应数据
     * @throws PayException 支付过程中发生的异常
     */
    String pagePay(String sn, BigDecimal amount, String subject) throws PayException;

    /**
     * 电脑网站支付
     * @param sn 平台订单号
     * @param amount 用户支付金额(单位: 元)
     * @param subject 商品标题
     * @param expireTime 超时时间("2022-08-01 22:00:00")
     * @return 支付页面HTML形式的响应数据
     * @throws PayException 支付过程中发生的异常
     */
    String pagePay(String sn, BigDecimal amount, String subject, String expireTime) throws PayException;

    /**
     * 电脑网站支付（使用模型对象）
     * @param model 支付模型对象
     * @return 支付页面HTML形式的响应数据
     * @throws PayException 支付过程中发生的异常
     */
    String pagePay(AlipayTradePagePayModel model) throws PayException;

    /**
     * 手机网站支付
     * @param sn 平台订单号
     * @param amount 用户支付金额(单位: 元)
     * @param subject 商品标题
     * @return 支付页面HTML形式的响应数据
     * @throws PayException 支付过程中发生的异常
     */
    String mobileWapPay(String sn, BigDecimal amount, String subject) throws PayException;

    /**
     * 手机网站支付
     * @param sn 平台订单号
     * @param amount 用户支付金额(单位: 元)
     * @param subject 商品标题
     * @param expireTime 超时时间("2022-08-01 22:00:00")
     * @return 支付页面HTML形式的响应数据
     * @throws PayException 支付过程中发生的异常
     */
    String mobileWapPay(String sn, BigDecimal amount, String subject, String expireTime) throws PayException;

    /**
     * 手机网站支付（使用模型对象）
     * @param model 支付模型对象
     * @return 支付页面HTML形式的响应数据
     * @throws PayException 支付过程中发生的异常
     */
    String mobileWapPay(AlipayTradeWapPayModel model) throws PayException;

    /**
     * APP支付
     * @param sn 平台订单号
     * @param amount 用户支付金额(单位: 元)
     * @param subject 商品标题
     * @return orderStr｜签名字符串
     * @throws PayException 支付异常
     */
    String appPay(String sn, BigDecimal amount, String subject) throws PayException;

    /**
     * APP支付（带超时时间）
     * @param sn 平台订单号
     * @param amount 用户支付金额(单位: 元)
     * @param subject 商品标题
     * @param expireTime 超时时间("2022-08-01 22:00:00")
     * @return orderStr｜签名字符串
     * @throws PayException 支付异常
     */
    String appPay(String sn, BigDecimal amount, String subject, String expireTime) throws PayException;

    /**
     * APP支付（使用模型对象）
     * @param model 支付模型对象
     * @return orderStr｜签名字符串
     * @throws PayException 支付过程中发生的异常
     */
    String appPay(AlipayTradeAppPayModel model) throws PayException;

    /**
     * 付款码支付
     * @param sn 平台订单号
     * @param amount 用户支付金额(单位: 元)
     * @param subject 商品标题
     * @param code 付款码内容(条形码或二维码内容)
     * @return com.alipay.api.response.AlipayTradePayResponse
     * @throws PayException 支付异常
     */
    AlipayTradePayResponse codePay(String sn, BigDecimal amount, String subject, String code) throws PayException;

    /**
     * 付款码支付（使用模型对象）
     * @param model 支付模型对象
     * @return com.alipay.api.response.AlipayTradePayResponse
     * @throws PayException 支付过程中发生的异常
     */
    AlipayTradePayResponse codePay(AlipayTradePayModel model) throws PayException;

    /**
     * 二维码付款
     * @param sn 平台订单号
     * @param amount 用户支付金额(单位: 元)
     * @param subject 商品标题
     * @return com.alipay.api.response.AlipayTradePrecreateResponse
     * @throws PayException 支付异常
     */
    AlipayTradePrecreateResponse qrPay(String sn, BigDecimal amount, String subject) throws PayException;

    /**
     * 二维码付款（使用模型对象）
     * @param model 支付模型对象
     * @return com.alipay.api.response.AlipayTradePrecreateResponse
     * @throws PayException 支付过程中发生的异常
     */
    AlipayTradePrecreateResponse qrPay(AlipayTradePrecreateModel model) throws PayException;

    /**
     * 交易查询
     *
     * @param sn 平台订单号
     * @return io.github.tuhe32.bin.pay.ali.domain.AliPayQuery
     * @throws PayException 异常
     */
    AliPayQuery query(String sn) throws PayException;

    /**
     * 交易查询 - 平台订单号和支付宝订单号不能同时为空
     *
     * @param sn      平台订单号
     * @param tradeNo 支付宝订单号
     * @return io.github.tuhe32.bin.pay.ali.domain.AliPayQuery
     * @throws PayException 异常
     */
    AliPayQuery query(String sn, String tradeNo) throws PayException;

    /**
     * 交易查询
     *
     * @param model 查询模型
     * @return io.github.tuhe32.bin.pay.ali.domain.AliPayQuery
     * @throws PayException 异常
     */
    AliPayQuery query(AlipayTradeQueryModel model) throws PayException;

    /**
     * 交易退款
     *
     * @param sn     平台订单号
     * @param amount 退款金额(单位: 元)
     * @return com.alipay.api.response.AlipayTradeRefundResponse
     * @throws PayException 异常
     */
    AlipayTradeRefundResponse refund(String sn, BigDecimal amount) throws PayException;

    /**
     * 交易退款 - 平台订单号和支付宝订单号不能同时为空
     *
     * @param sn            平台订单号
     * @param tradeNo       支付宝订单号
     * @param amount        退款金额(单位: 元)
     * @param outRequestNo  退款请求号(部分退款必传，退款请求号需保证在商户端不重复)
     * @return com.alipay.api.response.AlipayTradeRefundResponse
     * @throws PayException 异常
     */
    AlipayTradeRefundResponse refund(String sn, String tradeNo, BigDecimal amount, String outRequestNo) throws PayException;

    /**
     * 交易退款
     *
     * @param model 退款模型
     * @return com.alipay.api.response.AlipayTradeRefundResponse
     * @throws PayException 异常
     */
    AlipayTradeRefundResponse refund(AlipayTradeRefundModel model) throws PayException;

    /**
     * 支付异步通知
     *
     * @param request 请求
     * @return io.github.tuhe32.bin.pay.ali.domain.AliPayNotify
     * @throws PayException 异常
     */
    AliPayNotify notify(HttpServletRequest request) throws PayException;
}
