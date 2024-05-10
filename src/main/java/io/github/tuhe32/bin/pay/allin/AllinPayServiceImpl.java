package io.github.tuhe32.bin.pay.allin;

import io.github.tuhe32.bin.pay.allin.config.AllinPayConfig;
import io.github.tuhe32.bin.pay.allin.config.AllinPayConfigHolder;
import io.github.tuhe32.bin.pay.allin.domain.*;
import io.github.tuhe32.bin.pay.allin.util.PaymentChecker;
import io.github.tuhe32.bin.pay.allin.util.SignUtils;
import io.github.tuhe32.bin.pay.common.BasePayServiceImpl;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.github.tuhe32.bin.pay.allin.constants.AllinPayConstant.*;

/**
 * @author 刘斌
 * @date 2024/5/1 17:00
 */
@Slf4j
@SuppressWarnings({"spellcheck", "Duplicates"})
public class AllinPayServiceImpl extends BasePayServiceImpl implements AllinPayService {

    private static final Gson GSON = new GsonBuilder().create();

    public AllinPayServiceImpl() {
        super.setConfigHolder(new AllinPayConfigHolder());
    }

    /**
     * 统一收银台支付
     *
     * @param request 支付参数
     * @return io.github.tuhe32.bin.pay.allin.domain.AllinPayExtraData
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public AllinPayExtraData checkoutPay(AllinPayCheckoutRequest request) throws PayException {
        Map<String, String> params = new HashMap<>(24);
        params.put("reqsn", request.getReqSn());
        params.put("trxamt", BaseWxPayRequest.yuan2Fen(request.getTrxAmt()).toString());
        params.put("body", request.getBody());
        params.put("paytype", request.getPayType() == null ? "W06" : request.getPayType());
        params.put("isdirectpay", "1");
        params.put("version", "12");
        if (request.getRemark() != null) {
            params.put("remark", request.getRemark());
        }
        if (request.getValidTime() != null) {
            params.put("validtime", request.getValidTime().toString());
        }
        if (request.getLimitPay() != null && request.getLimitPay()) {
            params.put("limit_pay", "no_credit");
        }
        this.buildBaseParams(params, request);
        log.info("统一收银台【支付参数】：{}", GSON.toJson(params));
        return AllinPayExtraData.of(params);
    }

    /**
     * 统一支付
     *
     * @param request 支付参数
     * @return 支付串
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public String unitOrderPay(AllinPayUnitOrderRequest request) throws PayException {
        Map<String, String> params = new HashMap<>(24);
        params.put("reqsn", request.getReqSn());
        params.put("trxamt", BaseWxPayRequest.yuan2Fen(request.getTrxAmt()).toString());
        params.put("body", request.getBody());
        params.put("paytype", request.getPayType());
        params.put("version", "11");
        if (request.getSubOpenId() != null) {
            params.put("acct", request.getSubOpenId());
        }
        if (request.getSubAppId() != null) {
            params.put("sub_appid", request.getSubAppId());
        }
        if (request.getRemark() != null) {
            params.put("remark", request.getRemark());
        }
        if (request.getValidTime() != null) {
            params.put("validtime", request.getValidTime().toString());
        }
        if (request.getLimitPay() != null && request.getLimitPay()) {
            params.put("limit_pay", "no_credit");
        }
        String domainUrl = this.buildBaseParams(params, request);
        Map<String, String> responseMap = this.postAllin(domainUrl + UNIT_ORDER, params, "统一支付");
        if (!STATUS_SUCCESS.equals(responseMap.get(TRX_STATUS))) {
            log.error("本次交易未成功【请求参数】：{}", GSON.toJson(params));
            throw new PayException(responseMap.get("errmsg"));
        }
        return responseMap.get("payinfo");
    }

    /**
     * 统一扫码支付
     *
     * @param request 支付参数
     * @return 支付串
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public Boolean scanQrPay(AllinPayScanQrPayRequest request) throws PayException {
        Map<String, String> params = new HashMap<>(24);
        params.put("reqsn", request.getReqSn());
        params.put("trxamt", BaseWxPayRequest.yuan2Fen(request.getTrxAmt()).toString());
        params.put("authcode", request.getAuthCode());
        params.put("terminfo", GSON.toJson(request.getTermInfo()));
        params.put("body", request.getBody());
        params.put("version", "11");
        if (request.getRemark() != null) {
            params.put("remark", request.getRemark());
        }
        if (request.getLimitPay() != null && request.getLimitPay()) {
            params.put("limit_pay", "no_credit");
        }
        String domainUrl = this.buildBaseParams(params, request);
        Map<String, String> responseMap = this.postAllin(domainUrl + SCAN_QR_PAY, params, "统一扫码支付");
        if (STATUS_SUCCESS.equals(responseMap.get(TRX_STATUS))) {
            return Boolean.TRUE;
        } else if (STATUS_WAIT.equals(responseMap.get(TRX_STATUS))) {
            log.error("本次交易处理中【请求参数】：{}", GSON.toJson(params));
            PaymentChecker checker = new PaymentChecker(this::query, params);
            return checker.startCheckingPayment();
        } else {
            log.error("本次交易未成功【请求参数】：{}", GSON.toJson(params));
            throw new PayException(responseMap.get("errmsg"));
        }
    }

    public boolean query(Map<String, String> params) throws PayException {
        return this.query(new AllinPayQueryRequest(params)).isSuccess();
    }

    /**
     * 统一查询接口
     *
     * @param request 查询参数
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public AllinPayQuery query(AllinPayQueryRequest request) throws PayException {
        Map<String, String> params = new HashMap<>(24);
        params.put("reqsn", request.getReqSn());
        params.put("version", "11");
        String domainUrl = this.buildBaseParams(params, request);
        Map<String, String> responseMap = this.postAllin(domainUrl + QUERY_ORDER, params, "统一查询");
        if (STATUS_SUCCESS.equals(responseMap.get(TRX_STATUS))) {
            log.info("查询成功【请求参数】：{}", GSON.toJson(params));
            return AllinPayQuery.of(responseMap, true, false);
        } else if (STATUS_WAIT.equals(responseMap.get(TRX_STATUS))) {
            log.error("本次交易处理中【请求参数】：{}", GSON.toJson(params));
            return AllinPayQuery.of(responseMap, false, true);
        } else {
            log.error("查询交易失败【请求参数】：{}", GSON.toJson(params));
            throw new PayException(responseMap.get("errmsg"));
        }
    }

    /**
     * 交易结果通知
     *
     * @param request 通知参数
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public AllinPayNotify notify(HttpServletRequest request) throws PayException {
        Map<String, String> requestMap = this.getRequestMap(request);
        SignUtils.validSign(requestMap);
        if (!STATUS_SUCCESS.equals(requestMap.get(TRX_STATUS))) {
            log.error("支付失败【请求参数】：{}", GSON.toJson(requestMap));
            throw new PayException(requestMap.get(TRX_STATUS), requestMap.get("errmsg"));
        }
        log.info("支付成功【请求参数】：{}", GSON.toJson(requestMap));
        return AllinPayNotify.of(requestMap);
    }

    /**
     * 统一退款接口
     *
     * @param request 退款参数
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public void handleRefund(AllinPayRefundRequest request) throws PayException {
        boolean partialRefund = request.getTrxAmt().compareTo(request.getOrderAmount()) < 0;
        if (partialRefund) {
            // 部分退款
            this.refund(request);
            return;
        }
        boolean isSameDay = isSameDay(LocalDateTime.now(), request.getOrderDate());
        if (isSameDay) {
            // 同一天
            this.cancel(request);
        } else {
            this.refund(request);
        }
    }

    /**
     * 部分或隔天交易-退款接口
     *
     * @param request 退款参数
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public void refund(AllinPayRefundRequest request) throws PayException {
        Map<String, String> params = new HashMap<>(24);
        params.put("reqsn", request.getReqSn());
        params.put("trxamt", BaseWxPayRequest.yuan2Fen(request.getTrxAmt()).toString());
        params.put("oldreqsn", request.getOldReqSn());
        params.put("oldtrxid", request.getOldTrxId());
        params.put("remark", request.getRemark());
        params.put("version", "11");
        String domainUrl = this.buildBaseParams(params, request);
        Map<String, String> responseMap = this.postAllin(domainUrl + REFUND_ORDER, params, "统一退款");
        if (!STATUS_SUCCESS.equals(responseMap.get(TRX_STATUS))) {
            log.error("退款失败【请求参数】：{}", GSON.toJson(params));
            throw new PayException(responseMap.get(TRX_STATUS), responseMap.get("errmsg"));
        }
        log.info("退款成功【请求参数】：{}", GSON.toJson(params));
    }

    /**
     * 统一撤销接口
     * 只能撤销当天的交易，全额退款，实时返回退款结果
     *
     * @param request 退款参数
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public void cancel(AllinPayRefundRequest request) throws PayException {
        Map<String, String> params = new HashMap<>(24);
        params.put("reqsn", request.getReqSn());
        params.put("trxamt", BaseWxPayRequest.yuan2Fen(request.getTrxAmt()).toString());
        params.put("oldreqsn", request.getOldReqSn());
        params.put("oldtrxid", request.getOldTrxId());
        params.put("version", "11");
        String domainUrl = this.buildBaseParams(params, request);
        Map<String, String> responseMap = this.postAllin(domainUrl + CANCEL_ORDER, params, "统一撤销");
        if (!STATUS_SUCCESS.equals(responseMap.get(TRX_STATUS))) {
            log.error("撤销交易失败【请求参数】：{}", GSON.toJson(params));
            throw new PayException(responseMap.get(TRX_STATUS), responseMap.get("errmsg"));
        }
        log.info("撤销交易成功【请求参数】：{}", GSON.toJson(params));
    }

    /**
     * 终端信息采集接口
     *
     * @param request 终端信息参数
     * @throws PayException 支付过程中发生的异常
     */
    @Override
    public void addTerm(AllinPayAddTermRequest request) throws PayException {
        Map<String, String> params = new HashMap<>(24);
        params.put("termno", request.getTermNo());
        params.put("devicetype", request.getDeviceType() == null ? "11" : request.getDeviceType());
        if (StringUtils.isNotBlank(request.getTermSn())) {
            params.put("termsn", request.getTermSn());
        }
        params.put("operation", request.getOperation());
        params.put("termstate", request.getTermState());
        params.put("termaddress", request.getTermAddress());
        params.put("version", "12");
        String domainUrl = this.buildBaseParams(params, request);
        this.postAllin(domainUrl + ADD_TERM, params, "终端信息采集");
    }

    public String buildBaseParams(Map<String, String> params, BaseAllinPayRequest baseRequest) throws PayException {
        AllinPayConfig config = (AllinPayConfig) getConfig();
        if (StringUtils.isNotBlank(baseRequest.getOrgId())) {
            params.put("orgid", baseRequest.getOrgId());
        } else if (config.getOrgId() != null) {
            params.put("orgid", config.getOrgId());
        }
        if (StringUtils.isNotBlank(baseRequest.getAppId())) {
            params.put("appid", baseRequest.getAppId());
        } else {
            params.put("appid", config.getAppId());
        }
        if (StringUtils.isNotBlank(baseRequest.getCusId())) {
            params.put("cusid", baseRequest.getCusId());
        } else {
            params.put("cusid", config.getCusId());
        }
        if (StringUtils.isNotBlank(baseRequest.getSubbranch())) {
            params.put("subbranch", baseRequest.getSubbranch());
        } else if (config.getSubbranch() != null) {
            params.put("subbranch", config.getSubbranch());
        }
        if (StringUtils.isNotBlank(baseRequest.getNotifyUrl())) {
            params.put("notify_url", baseRequest.getNotifyUrl());
        } else {
            params.put("notify_url", config.getNotifyUrl());
        }
        if (baseRequest.getExtraData() != null) {
            params.putAll(baseRequest.getExtraData());
        }
        params.put("signtype", config.getSignType());
        params.put("randomstr", SignUtils.randomCode(8));
        params.put("sign", SignUtils.sign(params, config.getAppKey(), config.getSignType()));
        return config.getDomainUrl();
    }

    private Map<String, String> postAllin(String url, Map<String, String> params, String operate) throws PayException {
        try {
            String requestStr = SignUtils.buildRequestStr(params);
            String respStr = SignUtils.send(url, requestStr);
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> map = GSON.fromJson(respStr, type);
            if (CODE_SUCCESS.equals(map.get("retcode"))) {
                SignUtils.validSign(map);
                log.info("通联{}调用成功【请求数据】：{}\n【响应数据】：{}", operate, requestStr, respStr);
                return map;
            } else {
                throw new PayException(map.get("retcode"), map.get("retmsg"));
            }
        } catch (Exception e) {
            log.error("通联{}调用失败【请求数据】：{}\n【原因】：{}", operate, GSON.toJson(params), e.getMessage());
            throw (e instanceof PayException) ? (PayException) e : new PayException(e.getMessage(), e);
        }
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

    private static boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        return date1 != null && date2 != null && date1.toLocalDate().isEqual(date2.toLocalDate());
    }

}
