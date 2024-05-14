package io.github.tuhe32.bin.pay.ums.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/14 11:06
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class UmsPayNotify {

    public static UmsPayNotify of(Map<String, String> map) {
        UmsPayNotify umsPayNotify = new UmsPayNotify();
        umsPayNotify.setMid(map.get("mid"));
        umsPayNotify.setTid(map.get("tid"));
        umsPayNotify.setInstMid(map.get("instMid"));
        umsPayNotify.setBillFunds(map.get("billFunds"));
        umsPayNotify.setBillFundsDesc(map.get("billFundsDesc"));
        umsPayNotify.setBuyerPayAmount(map.get("buyerPayAmount") != null ? Integer.valueOf(map.get("buyerPayAmount")) : null);
        umsPayNotify.setTotalAmount(map.get("totalAmount") != null ? Integer.valueOf(map.get("totalAmount")) : null);
        umsPayNotify.setMerOrderId(map.get("merOrderId"));
        umsPayNotify.setPayTime(map.get("payTime"));
        umsPayNotify.setReceiptAmount(map.get("receiptAmount") != null ? Integer.valueOf(map.get("receiptAmount")) : null);
        umsPayNotify.setRefundAmount(map.get("refundAmount") != null ? Integer.valueOf(map.get("refundAmount")) : null);
        umsPayNotify.setRefundDesc(map.get("refundDesc"));
        umsPayNotify.setSeqId(map.get("seqId"));
        umsPayNotify.setSettleDate(map.get("settleDate"));
        umsPayNotify.setStatus(map.get("status"));
        umsPayNotify.setTargetOrderId(map.get("targetOrderId"));
        umsPayNotify.setTargetSys(map.get("targetSys"));
        umsPayNotify.setRefundPayTime(map.get("refundPayTime"));
        umsPayNotify.setRefundSettleDate(map.get("refundSettleDate"));
        umsPayNotify.setOrderDesc(map.get("orderDesc"));
        umsPayNotify.setCreateTime(map.get("createTime"));
        umsPayNotify.setRefundOrderId(map.get("refundOrderId"));
        return umsPayNotify;
    }

    /**
     * 商户号
     */
    private String mid;
    /**
     * 终端号
     */
    private String tid;
    /**
     * 业务类型
     */
    private String instMid;
    /**
     * 资金渠道
     */
    private String billFunds;
    /**
     * 资金渠道说明
     */
    private String billFundsDesc;
    /**
     * 买家实付金额
     */
    private Integer buyerPayAmount;
    /**
     * 订单金额，单位分
     */
    private Integer totalAmount;
    /**
     * 商户订单号
     */
    private String merOrderId;
    /**
     * 支付时间
     */
    private String payTime;
    /**
     * 实收金额，单位分
     */
    private Integer receiptAmount;
    /**
     * 退款金额，单位分
     */
    private Integer refundAmount;
    /**
     * 退款说明
     */
    private String refundDesc;
    /**
     * 系统交易流水号
     */
    private String seqId;
    /**
     * 结算日期yyyy-MM-dd
     */
    private String settleDate;
    /**
     * 订单状态
     */
    private String status;
    /**
     * 渠道订单号
     */
    private String targetOrderId;
    /**
     * 支付渠道
     */
    private String targetSys;
    /**
     * 退款时间
     */
    private String refundPayTime;
    /**
     * 退款结算日期yyyy-MM-dd
     */
    private String refundSettleDate;
    /**
     * 订单描述
     */
    private String orderDesc;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 退款订单号
     */
    private String refundOrderId;


}
