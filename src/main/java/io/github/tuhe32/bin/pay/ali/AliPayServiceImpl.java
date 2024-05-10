package io.github.tuhe32.bin.pay.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.json.JSONWriter;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import io.github.tuhe32.bin.pay.ali.config.AliPayConfig;
import io.github.tuhe32.bin.pay.ali.config.AliPayConfigHolder;
import io.github.tuhe32.bin.pay.ali.constants.AliPayConstant;
import io.github.tuhe32.bin.pay.ali.domain.AliPayNotify;
import io.github.tuhe32.bin.pay.ali.domain.AliPayQuery;
import io.github.tuhe32.bin.pay.ali.enums.TradeStatus;
import io.github.tuhe32.bin.pay.common.exception.CheckedFunction;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 刘斌
 * @date 2024/4/26 09:30
 */
@Slf4j
public class AliPayServiceImpl implements AliPayService {

    protected Map<String, AliPayConfig> configMap = new HashMap<>();

    @Override
    public AliPayConfig getConfig() {
        if (this.configMap.size() == 1) {
            // 只有一个商户号，直接返回其配置即可
            return this.configMap.values().iterator().next();
        }
        return this.configMap.get(AliPayConfigHolder.get());
    }

    @Override
    public void setConfig(AliPayConfig config) {
        final String defaultAppId = config.getAppId();
        this.setMultiConfig(Map.of(defaultAppId, config), defaultAppId);
    }

    @Override
    public void addConfig(String appId, AliPayConfig aliPayConfig) {
        synchronized (this) {
            if (this.configMap == null) {
                this.setConfig(aliPayConfig);
            } else {
                AliPayConfigHolder.set(appId);
                this.configMap.put(appId, aliPayConfig);
            }
        }
    }

    @Override
    public void removeConfig(String appId) {
        synchronized (this) {
            if (this.configMap.size() == 1) {
                this.configMap.remove(appId);
                log.warn("已删除最后一个商户号配置：{}，须立即使用setConfig或setMultiConfig添加配置", appId);
                return;
            }
            if (AliPayConfigHolder.get().equals(appId)) {
                this.configMap.remove(appId);
                final String defaultAppId = this.configMap.keySet().iterator().next();
                AliPayConfigHolder.set(defaultAppId);
                log.warn("已删除默认商户号配置，商户号【{}】被设为默认配置", defaultAppId);
                return;
            }
            this.configMap.remove(appId);
        }
    }

    @Override
    public void setMultiConfig(Map<String, AliPayConfig> aliPayConfig) {
        this.setMultiConfig(aliPayConfig, aliPayConfig.keySet().iterator().next());
    }

    @Override
    public void setMultiConfig(Map<String, AliPayConfig> aliPayConfig, String defaultAppId) {
        this.configMap = new HashMap<>(aliPayConfig);
        AliPayConfigHolder.set(defaultAppId);
    }

    @Override
    public boolean switchover(String appId) {
        if (this.configMap.containsKey(appId)) {
            AliPayConfigHolder.set(appId);
            return true;
        }
        log.error("无法找到对应【{}】的商户号配置信息，请核实！", appId);
        return false;
    }

    @Override
    public boolean switchoverTo(String appId) throws PayException {
        if (this.configMap.containsKey(appId)) {
            AliPayConfigHolder.set(appId);
            return true;
        }
        throw new PayException(String.format("无法找到对应【%s】的商户号配置信息，请核实！", appId));
    }

    @Override
    public String pagePay(String sn, BigDecimal amount, String subject) throws PayException {
        return this.pagePay(sn, amount, subject, null);
    }

    @Override
    public String pagePay(String sn, BigDecimal amount, String subject, String expireTime) throws PayException {
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(sn);
        model.setTotalAmount(amount.toPlainString());
        model.setSubject(subject);
        model.setProductCode(AliPayConstant.PAGE_PRODUCT_CODE);
        model.setTimeExpire(expireTime);
        return this.pagePay(model);
    }

    /**
     * 电脑网站支付
     */
    @Override
    public String pagePay(AlipayTradePagePayModel model) throws PayException {
        AliPayConfig config = this.getConfig();
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl(config.getNotifyUrl());
        //同步跳转地址，仅支持http/https
        request.setReturnUrl(config.getReturnUrl());
        request.setBizModel(model);
        // 网页支付方式, 将 返回值 .getBody() 内容作为 form 表单进行提交, 会跳转到一个 url, 具体参考文档
        return this.postAlipay(config.getClient()::pageExecute, request).getBody();
//        try {
//            AlipayTradePagePayResponse response = config.getClient().pageExecute(request);
//            if (response.isSuccess()) {
//                String pageRedirectionData = response.getBody();
//                log.info("支付宝调用成功【请求数据】：{}\n【响应数据】：{}", writeJson(request.getBizModel()), pageRedirectionData);
//                return pageRedirectionData;
//            } else {
//                log.error("支付宝调用失败【请求数据】：{}\n【原因】：{}", writeJson(request.getBizModel()), response.getSubMsg());
//                throw new PayException(response.getSubCode(), response.getSubMsg());
//            }
//        } catch (Exception e) {
//            log.error("支付宝调用失败【请求数据】：{}\n【原因】：{}", writeJson(request.getBizModel()), e.getMessage());
//            throw (e instanceof PayException) ? (PayException) e : new PayException(e.getMessage(), e);
//        }
    }

    @Override
    public String mobileWapPay(String sn, BigDecimal amount, String subject) throws PayException {
        return this.mobileWapPay(sn, amount, subject, null);
    }

    @Override
    public String mobileWapPay(String sn, BigDecimal amount, String subject, String expireTime) throws PayException {
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(sn);
        model.setTotalAmount(amount.toPlainString());
        model.setSubject(subject);
        model.setProductCode(AliPayConstant.WAP_PRODUCT_CODE);
        model.setTimeExpire(expireTime);
        return this.mobileWapPay(model);
    }

    /**
     * 手机网站支付
     */
    @Override
    public String mobileWapPay(AlipayTradeWapPayModel model) throws PayException {
        AliPayConfig config = this.getConfig();
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        //异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl(config.getNotifyUrl());
        //同步跳转地址，仅支持http/https
        request.setReturnUrl(config.getReturnUrl());
        request.setBizModel(model);
        // 网页支付方式, 将 返回值 .getBody() 内容作为 form 表单进行提交, 会跳转到一个 url, 具体参考文档
        return this.postAlipay(config.getClient()::pageExecute, request).getBody();
//        try {
//            AlipayTradeWapPayResponse response = config.getClient().pageExecute(request);
//            if (response.isSuccess()) {
//                String pageRedirectionData = response.getBody();
//                log.info("支付宝调用成功【请求数据】：{}\n【响应数据】：{}", writeJson(request.getBizModel()), pageRedirectionData);
//                return pageRedirectionData;
//            } else {
//                log.error("支付宝调用失败【请求数据】：{}\n【原因】：{}", writeJson(request.getBizModel()), response.getSubMsg());
//                throw new PayException(response.getSubCode(), response.getSubMsg());
//            }
//        } catch (Exception e) {
//            log.error("支付宝调用失败【请求数据】：{}\n【原因】：{}", writeJson(request.getBizModel()), e.getMessage());
//            throw (e instanceof PayException) ? (PayException) e : new PayException(e.getMessage(), e);
//        }
    }

    /**
     * APP支付
     *
     * @param sn      平台订单号
     * @param amount  用户支付金额(单位: 元)
     * @param subject 商品标题
     * @return orderStr｜签名字符串
     */
    @Override
    public String appPay(String sn, BigDecimal amount, String subject) throws PayException {
        return this.appPay(sn, amount, subject, null);
    }

    /**
     * APP支付
     *
     * @param sn         平台订单号
     * @param amount     用户支付金额(单位: 元)
     * @param subject    商品标题
     * @param expireTime 超时时间("2022-08-01 22:00:00")
     * @return orderStr｜签名字符串
     */
    @Override
    public String appPay(String sn, BigDecimal amount, String subject, String expireTime) throws PayException {
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(sn);
        model.setTotalAmount(amount.toPlainString());
        model.setSubject(subject);
        model.setTimeExpire(expireTime);
        return this.appPay(model);
    }

    /**
     * APP支付
     */
    @Override
    public String appPay(AlipayTradeAppPayModel model) throws PayException {
        AliPayConfig config = this.getConfig();
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl(config.getNotifyUrl());
        request.setBizModel(model);
        // 网页支付方式, 将 返回值 .getBody() 内容作为 form 表单进行提交, 会跳转到一个 url, 具体参考文档
        return this.postAlipay(config.getClient()::sdkExecute, request).getBody();
//        try {
//            AlipayTradeAppPayResponse response = config.getClient().sdkExecute(request);
//            if (response.isSuccess()) {
//                String orderStr = response.getBody();
//                log.info("支付宝调用成功【请求数据】：{}\n【响应数据】：{}", writeJson(request.getBizModel()), orderStr);
//                return orderStr;
//            } else {
//                throw new PayException(response.getSubCode(), response.getSubMsg());
//            }
//        } catch (Exception e) {
//            log.error("支付宝调用失败【请求数据】：{}\n【原因】：{}", writeJson(request.getBizModel()), e.getMessage());
//            throw (e instanceof PayException) ? (PayException) e : new PayException(e.getMessage(), e);
//        }
    }

    /**
     * 付款码支付
     *
     * @param sn      平台订单号
     * @param amount  用户支付金额(单位: 元)
     * @param subject 商品标题
     * @param code    付款码内容(条形码或二维码内容)
     * @return com.alipay.api.response.AlipayTradePayResponse
     */
    @Override
    public AlipayTradePayResponse codePay(String sn, BigDecimal amount, String subject, String code) throws PayException {
        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setOutTradeNo(sn);
        model.setTotalAmount(amount.toPlainString());
        model.setSubject(subject);
        // 付款码内容
        model.setAuthCode(code);
        return this.codePay(model);
    }

    /**
     * 付款码支付
     */
    @Override
    public AlipayTradePayResponse codePay(AlipayTradePayModel model) throws PayException {
        AliPayConfig config = this.getConfig();
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setBizModel(model);
        // 付款码场景固定
        model.setScene("bar_code");
        request.setNotifyUrl(config.getNotifyUrl());
        return this.postAlipay(config.getClient()::execute, request);
    }

    /**
     * 二维码付款
     *
     * @param sn      平台订单号
     * @param amount  用户支付金额(单位: 元)
     * @param subject 商品标题
     * @return com.alipay.api.response.AlipayTradePrecreateResponse
     */
    @Override
    public AlipayTradePrecreateResponse qrPay(String sn, BigDecimal amount, String subject) throws PayException {
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setOutTradeNo(sn);
        model.setTotalAmount(amount.toPlainString());
        model.setSubject(subject);
        return this.qrPay(model);
    }

    /**
     * 二维码付款
     */
    @Override
    public AlipayTradePrecreateResponse qrPay(AlipayTradePrecreateModel model) throws PayException {
        AliPayConfig config = this.getConfig();
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(config.getNotifyUrl());
        return this.postAlipay(config.getClient()::execute, request);
    }

    /**
     * 交易查询
     *
     * @param sn 平台订单号
     * @return io.github.tuhe32.bin.pay.ali.domain.AliPayQuery
     */
    @Override
    public AliPayQuery query(String sn) throws PayException {
        return this.query(sn, null);
    }

    /**
     * 交易查询 - 平台订单号和支付宝订单号不能同时为空
     *
     * @param sn      平台订单号
     * @param tradeNo 支付宝订单号
     * @return io.github.tuhe32.bin.pay.ali.domain.AliPayQuery
     */
    @Override
    public AliPayQuery query(String sn, String tradeNo) throws PayException {
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(sn);
        model.setTradeNo(tradeNo);
        return this.query(model);
    }

    /**
     * 交易查询
     *
     * @return io.github.tuhe32.bin.pay.ali.domain.AliPayQuery
     */
    @Override
    public AliPayQuery query(AlipayTradeQueryModel model) throws PayException {
        AliPayConfig config = this.getConfig();
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(model);
        AlipayTradeQueryResponse queryResponse = this.postAlipay(config.getClient()::execute, request);
        if (!TradeStatus.SUCCESS.getStr().equals(queryResponse.getTradeStatus())) {
            log.error("本次查询交易未成功【返回数据】：{}", writeJson(queryResponse));
            throw new PayException("本次交易未成功，"+ queryResponse.getTradeStatus());
        }
        return AliPayQuery.of(queryResponse);
    }

    /**
     * 交易退款
     *
     * @param sn     平台订单号
     * @param amount 退款金额(单位: 元)
     * @return com.alipay.api.response.AlipayTradeRefundResponse
     */
    @Override
    public AlipayTradeRefundResponse refund(String sn, BigDecimal amount) throws PayException {
        return this.refund(sn, null, amount, null);
    }

    /**
     * 交易退款 - 平台订单号和支付宝订单号不能同时为空
     *
     * @param sn           平台订单号
     * @param tradeNo      支付宝订单号
     * @param amount       退款金额(单位: 元)
     * @param outRequestNo 退款请求号(部分退款必传，退款请求号需保证在商户端不重复)
     * @return com.alipay.api.response.AlipayTradeRefundResponse
     */
    @Override
    public AlipayTradeRefundResponse refund(String sn, String tradeNo, BigDecimal amount, String outRequestNo) throws PayException {
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(sn);
        model.setTradeNo(tradeNo);
        model.setRefundAmount(amount.toPlainString());
        model.setOutRequestNo(outRequestNo);
        return this.refund(model);
    }

    /**
     * 交易退款
     *
     * @return com.alipay.api.response.AlipayTradeRefundResponse
     */
    @Override
    public AlipayTradeRefundResponse refund(AlipayTradeRefundModel model) throws PayException {
        AliPayConfig config = this.getConfig();
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(model);
        AlipayTradeRefundResponse response = this.postAlipay(config.getClient()::execute, request);
        if (!Objects.equals(response.getFundChange(), "Y")) {
            log.error("本次退款尚未成功【返回数据】：{}", writeJson(response));
            throw new PayException("本次退款尚未成功，请调用退款查询接口重新查询退款状态");
        }
        return response;
    }

    /**
     * 支付异步通知
     *
     * @param request 请求
     * @return io.github.tuhe32.bin.pay.ali.domain.AliPayNotify
     * @throws PayException 异常
     */
    @Override
    public AliPayNotify notify(HttpServletRequest request) throws PayException {
        Map<String, String> requestMap = this.getRequestMap(request);
        this.switchoverTo(requestMap.get("app_id"));
        this.checkSignV1(requestMap);
        if (!TradeStatus.SUCCESS.getStr().equals(requestMap.get("trade_status"))) {
            log.error("支付失败【请求参数】：{}", writeJson(requestMap, false));
            throw new PayException(requestMap.get("trade_status"), "支付失败");
        }
        return AliPayNotify.of(requestMap);
    }

    /**
     * 获取HttpServletRequest里面的参数
     */
    public Map<String, String> getRequestMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * v1 版本验签
     *
     * @param map 所有参数
     */
    public void checkSignV1(Map<String, String> map) throws PayException {
        AliPayConfig config = this.getConfig();
        try {
            //调用SDK验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(map, config.getAlipayPublicKey(), config.getCharset(), config.getSignType());
            if (!signVerified) {
                throw new PayException("非法请求，支付宝验签失败");
            }
        } catch (Exception e) {
            log.error("支付宝验签失败【请求数据】：{}\n【原因】：{}", writeJson(map, false), "验签失败");
            throw (e instanceof PayException) ? (PayException) e : new PayException(e.getMessage(), e);
        }
    }

    private <T extends AlipayResponse, R extends AlipayRequest<T>> T postAlipay(CheckedFunction<R, T, AlipayApiException> function, R request) throws PayException {
        try {
            T response = function.apply(request);
            if (response.isSuccess()) {
                log.info("支付宝调用成功【请求数据】：{}\n【响应数据】：{}", writeJson(request.getBizModel()), response.getBody() == null ? writeJson(response) : response.getBody());
                return response;
            } else {
                throw new PayException(response.getSubCode(), response.getSubMsg());
            }
        } catch (Exception e) {
            log.error("支付宝调用失败【请求数据】：{}\n【原因】：{}", writeJson(request.getBizModel()), e.getMessage());
            throw (e instanceof PayException) ? (PayException) e : new PayException(e.getMessage(), e);
        }
    }


    private String writeJson(Object model) {
        return this.writeJson(model, true);
    }

    private String writeJson(Object model, boolean isApiModel) {
        return new JSONWriter().write(model, isApiModel);
    }

}
