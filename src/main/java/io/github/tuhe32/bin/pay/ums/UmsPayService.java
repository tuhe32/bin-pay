package io.github.tuhe32.bin.pay.ums;

import io.github.tuhe32.bin.pay.common.BasePayConfig;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import io.github.tuhe32.bin.pay.ums.domain.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/11 15:39
 */
public interface UmsPayService {

    /**
     * 获取配置
     *
     * @return 可能为null的BasePayConfig对象
     */
    BasePayConfig getConfig();

    /**
     * 设置配置
     *
     * @param config 配置对象
     */
    void setConfig(BasePayConfig config);

    /**
     * 添加配置
     *
     * @param appId         应用ID
     * @param basePayConfig 配置对象
     */
    void addConfig(String appId, BasePayConfig basePayConfig);

    /**
     * 移除配置
     *
     * @param appId 应用ID
     */
    void removeConfig(String appId);

    /**
     * 设置多配置
     *
     * @param basePayConfigMap 多个配置的映射表
     */
    void setMultiConfig(Map<String, BasePayConfig> basePayConfigMap);

    /**
     * 设置多配置并指定默认应用ID
     *
     * @param basePayConfigMap 多个配置的映射表
     * @param defaultCusId     默认应用ID
     */
    void setMultiConfig(Map<String, BasePayConfig> basePayConfigMap, String defaultCusId);

    /**
     * 切换至指定应用ID的配置
     *
     * @param appId 应用ID
     * @return 是否切换成功
     */
    boolean switchover(String appId);

    /**
     * 切换至指定应用ID的配置并返回当前对象
     *
     * @param appId 应用ID
     * @return 当前AliPayService实例
     * @throws PayException 如果找不到指定应用ID的配置
     */
    boolean switchoverTo(String appId) throws PayException;

    /**
     * 小程序支付
     *
     * @param request 小程序支付请求
     * @return UmsPayMiniResponse
     * @throws PayException 支付过程中发生的异常
     */
    UmsPayMiniResponse miniPay(UmsPayMiniRequest request) throws PayException;

    /**
     * APP支付
     *
     * @param request APP支付请求
     * @return UmsPayAppResponse
     * @throws PayException 支付过程中发生的异常
     */
    UmsPayAppResponse appPay(UmsPayAppRequest request) throws PayException;

    /**
     * H5支付
     * 由浏览器直接跳转，不需要应答报文
     * 生成支付链接的形式，用户访问这个链接，跳转到一个页面上进行支付（跳转支付）
     *
     * @param request H5支付请求
     * @return 支付链接
     * @throws PayException 支付过程中发生的异常
     */
    String h5Pay(UmsPayMiniRequest request) throws PayException;

    /**
     * JS支付
     * 返回报文中的参数，调用微信的JSAPI支付方式，拉起支付
     *
     * @param request JS支付请求
     * @return UmsPayJsResponse
     * @throws PayException 支付过程中发生的异常
     */
    UmsPayJsResponse jsPay(UmsPayMiniRequest request) throws PayException;

    /**
     * 扫码支付
     *
     * @param request 扫码支付请求
     * @return UmsPayScanQrPayResponse
     * @throws PayException 支付过程中发生的异常
     */
    UmsPayScanQrPayResponse scanQrPay(UmsPayScanQrPayRequest request) throws PayException;

    /**
     * 小程序支付查询
     *
     * @param request 小程序支付查询请求
     * @return UmsPayQueryResponse
     * @throws PayException 支付过程中发生的异常
     */
    UmsPayQueryResponse miniQuery(UmsPayQueryRequest request) throws PayException;

    /**
     * JS支付查询
     *
     * @param request JS支付查询请求
     * @return UmsPayQueryResponse
     * @throws PayException 支付过程中发生的异常
     */
    UmsPayQueryResponse jsQuery(UmsPayQueryRequest request) throws PayException;

    /**
     * APP支付查询
     *
     * @param request APP支付查询请求
     * @return UmsPayQueryResponse
     * @throws PayException 支付过程中发生的异常
     */
    UmsPayQueryResponse appQuery(UmsPayQueryRequest request) throws PayException;

    /**
     * 小程序或H5,JS退款
     *
     * @param request 小程序或H5,JS退款退款请求
     * @return UmsPayRefundResponse
     * @throws PayException 支付过程中发生的异常
     */
    UmsPayRefundResponse miniRefund(UmsPayRefundRequest request) throws PayException;

    /**
     * APP退款
     *
     * @param request APP退款请求
     * @return UmsPayRefundResponse
     * @throws PayException 支付过程中发生的异常
     */
    UmsPayRefundResponse appRefund(UmsPayRefundRequest request) throws PayException;

    /**
     * 扫码支付退款
     *
     * @param request 扫码支付退款请求
     * @return UmsPayScanQrRefundResponse
     * @throws PayException 支付过程中发生的异常
     */
    UmsPayScanQrRefundResponse scanQrRefund(UmsPayScanQrRefundRequest request) throws PayException;

    /**
     * 支付或退款回调
     *
     * @param request 扫码支付回调请求
     * @return UmsPayNotify
     * @throws PayException 支付过程中发生的异常
     */
    UmsPayNotify notify(HttpServletRequest request) throws PayException;

}
