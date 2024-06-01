package io.github.tuhe32.bin.pay.allin;

import io.github.tuhe32.bin.pay.allin.domain.*;
import io.github.tuhe32.bin.pay.common.BasePayConfig;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/1 17:00
 */
public interface AllinPayService {

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
     * 清除在ThreadLocal中的配置数据
     */
    void clearConfigHolder();

    /**
     * 统一收银台支付
     *
     * @param request 支付参数
     * @return io.github.tuhe32.bin.pay.allin.domain.AllinPayExtraData
     * @throws PayException 支付过程中发生的异常
     */
    AllinPayExtraData checkoutPay(AllinPayCheckoutRequest request) throws PayException;

    /**
     * H5收银台支付
     *
     * @param request 支付参数
     * @return 前端需要重定向的地址
     * @throws PayException 支付过程中发生的异常
     */
    String h5CheckoutPay(AllinPayH5CheckoutRequest request) throws PayException;

    /**
     * 统一支付
     *
     * @param request 支付参数
     * @return 支付串
     * @throws PayException 支付过程中发生的异常
     */
    String unitOrderPay(AllinPayUnitOrderRequest request) throws PayException;

    /**
     * 统一扫码支付
     *
     * @param request 支付参数
     * @return 支付串
     * @throws PayException 支付过程中发生的异常
     */
    Boolean scanQrPay(AllinPayScanQrPayRequest request) throws PayException;

    /**
     * 统一查询接口
     *
     * @param request 查询参数
     * @return 查询结果
     * @throws PayException 支付过程中发生的异常
     */
    AllinPayQuery query(AllinPayQueryRequest request) throws PayException;

    /**
     * 交易结果通知
     *
     * @param request 通知参数
     * @return 通知结果
     * @throws PayException 支付过程中发生的异常
     */
    AllinPayNotify notify(HttpServletRequest request) throws PayException;

    /**
     * 终端信息采集接口
     *
     * @param request 终端信息参数
     * @throws PayException 支付过程中发生的异常
     */
    void addTerm(AllinPayAddTermRequest request) throws PayException;

    /**
     * 统一退款接口
     *
     * @param request 退款参数
     * @throws PayException 支付过程中发生的异常
     */
    void handleRefund(AllinPayRefundRequest request) throws PayException;

    /**
     * 部分或隔天交易-退款接口
     *
     * @param request 退款参数
     * @throws PayException 支付过程中发生的异常
     */
    void refund(AllinPayRefundRequest request) throws PayException;

    /**
     * 统一撤销接口
     * 只能撤销当天的交易，全额退款，实时返回退款结果
     *
     * @param request 撤销请求参数
     * @throws PayException 支付过程中发生的异常
     */
    void cancel(AllinPayRefundRequest request) throws PayException;

}
