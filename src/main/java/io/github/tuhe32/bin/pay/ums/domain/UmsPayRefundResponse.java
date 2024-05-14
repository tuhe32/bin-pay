package io.github.tuhe32.bin.pay.ums.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/14 10:36
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class UmsPayRefundResponse {

    public static UmsPayRefundResponse of(Map<String, String> map) {
        UmsPayRefundResponse umsPayRefundResponse = new UmsPayRefundResponse();
        umsPayRefundResponse.setMid(map.get("mid"));
        umsPayRefundResponse.setResponseTimeStamp(map.get("responseTimeStamp"));
        umsPayRefundResponse.setRefundStatus(map.get("refundStatus"));
        umsPayRefundResponse.setSeqId(map.get("seqId"));
        umsPayRefundResponse.setStatus(map.get("status"));
        umsPayRefundResponse.setMerOrderId(map.get("merOrderId"));
        umsPayRefundResponse.setTargetOrderId(map.get("targetOrderId"));
        umsPayRefundResponse.setTargetStatus(map.get("targetStatus"));
        umsPayRefundResponse.setTargetSys(map.get("targetSys"));
        umsPayRefundResponse.setTotalAmount(map.get("totalAmount") == null ? null : Integer.parseInt(map.get("totalAmount")));
        umsPayRefundResponse.setRefundAmount(map.get("refundAmount") == null ? null : Integer.parseInt(map.get("refundAmount")));
        umsPayRefundResponse.setRefundFunds(map.get("refundFunds"));
        umsPayRefundResponse.setRefundOrderId(map.get("refundOrderId"));
        umsPayRefundResponse.setRefundTargetOrderId(map.get("refundTargetOrderId"));
        return umsPayRefundResponse;
    }

    /**
     * 商户号
     */
    private String mid;
    /**
     * 响应时间戳
     */
    private String responseTimeStamp;
    /**
     * 退款状态
     */
    private String refundStatus;
    /**
     * 商户订单号
     */
    private String merOrderId;
    /**
     * 平台流水号
     */
    private String seqId;
    /**
     * 交易状态
     */
    private String status;
    /**
     * 目标平台单号
     */
    private String targetOrderId;
    /**
     * 目标平台状态
     */
    private String targetStatus;
    /**
     * 退款目标系统
     */
    private String targetSys;
    /**
     * 支付总金额
     */
    private Integer totalAmount;
    /**
     * 退款总金额
     */
    private Integer refundAmount;
    /**
     * 退款渠道列表
     */
    private String refundFunds;
    /**
     * 退款订单号
     */
    private String refundOrderId;
    /**
     * 目标系统退货订单号
     */
    private String refundTargetOrderId;
}
