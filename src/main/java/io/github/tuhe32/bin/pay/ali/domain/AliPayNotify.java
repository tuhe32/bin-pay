package io.github.tuhe32.bin.pay.ali.domain;

import io.github.tuhe32.bin.pay.ali.enums.TradeStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/4/27 15:10
 */
@Getter
@ToString
@Accessors(chain = true)
@Setter(AccessLevel.PRIVATE)
public class AliPayNotify {

    /**
     * 解析回调参数
     *
     * @param callbackParams 所有回调参数
     * @return 阿里支付回调信息
     */
    public static AliPayNotify of(Map<String, String> callbackParams) {
        AliPayNotify aliPayNotify = new AliPayNotify();
        aliPayNotify.setGmtCreate(callbackParams.get("gmt_create"));
        aliPayNotify.setCharset(callbackParams.get("charset"));
        aliPayNotify.setSellerEmail(callbackParams.get("seller_email"));
        aliPayNotify.setSubject(callbackParams.get("subject"));
        aliPayNotify.setSign(callbackParams.get("sign"));
        aliPayNotify.setBuyerId(callbackParams.get("buyer_id"));
        aliPayNotify.setInvoiceAmount(new BigDecimal(callbackParams.get("invoice_amount")));
        aliPayNotify.setNotifyId(callbackParams.get("notify_id"));

        // Handle FundBill list separately
        String fundBillListStr = callbackParams.get("fund_bill_list").replace("&quot;", "\"");
        aliPayNotify.setFundBillList(fundBillListStr);

        aliPayNotify.setNotifyType(callbackParams.get("notify_type"));
        aliPayNotify.setTradeStatus(TradeStatus.of(callbackParams.get("trade_status")));
        aliPayNotify.setReceiptAmount(new BigDecimal(callbackParams.get("receipt_amount")));
        aliPayNotify.setAppId(callbackParams.get("app_id"));
        aliPayNotify.setBuyerPayAmount(new BigDecimal(callbackParams.get("buyer_pay_amount")));
        aliPayNotify.setSignType(callbackParams.get("sign_type"));
        aliPayNotify.setSellerId(callbackParams.get("seller_id"));
        aliPayNotify.setGmtPayment(callbackParams.get("gmt_payment"));
        aliPayNotify.setNotifyTime(callbackParams.get("notify_time"));
        aliPayNotify.setVersion(callbackParams.get("version"));
        aliPayNotify.setOutTradeNo(callbackParams.get("out_trade_no"));
        aliPayNotify.setTotalAmount(new BigDecimal(callbackParams.get("total_amount")));
        aliPayNotify.setTradeNo(callbackParams.get("trade_no"));
        aliPayNotify.setAuthAppId(callbackParams.get("auth_app_id"));
        aliPayNotify.setBuyerLogonId(callbackParams.get("buyer_logon_id"));
        aliPayNotify.setPointAmount(new BigDecimal(callbackParams.get("point_amount")));
        aliPayNotify.setRaw(callbackParams);

        return aliPayNotify;
    }

    private Map<String, String> raw;

    private String gmtCreate;

    private String charset;

    private String sellerEmail;

    private String subject;

    private String sign;

    private String buyerId;

    private BigDecimal invoiceAmount;

    private String notifyId;

    private String fundBillList;

    private String notifyType;

    private TradeStatus tradeStatus;

    private BigDecimal receiptAmount;

    private String appId;

    private BigDecimal buyerPayAmount;

    private String signType;

    private String sellerId;

    private String gmtPayment;

    private String notifyTime;

    private String version;

    private String outTradeNo;

    private BigDecimal totalAmount;

    private String tradeNo;

    private String authAppId;

    private String buyerLogonId;

    private BigDecimal pointAmount;

//    @Data
//    public static class FundBill {
//
//        private BigDecimal amount;
//
//        private String fundChannel;
//
//    }
}
