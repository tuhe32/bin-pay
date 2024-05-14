package io.github.tuhe32.bin.pay.ums.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/14 15:23
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class UmsPayJsResponse {

    public static UmsPayJsResponse of(Map<String, String> map) {
        UmsPayJsResponse umsPayJsResponse = new UmsPayJsResponse();
        umsPayJsResponse.setJsPayRequest(map.get("jsPayRequest"));
        umsPayJsResponse.setTargetOrderId(map.get("targetOrderId"));
        umsPayJsResponse.setMerOrderId(map.get("merOrderId"));
        umsPayJsResponse.setTotalAmount(map.get("totalAmount") == null ? null : Integer.valueOf(map.get("totalAmount")));
        umsPayJsResponse.setStatus(map.get("status"));
        umsPayJsResponse.setTargetSys(map.get("targetSys"));
        umsPayJsResponse.setTargetStatus(map.get("targetStatus"));
        umsPayJsResponse.setSeqId(map.get("seqId"));
        umsPayJsResponse.setRedirectUrl(map.get("redirectUrl"));
        return umsPayJsResponse;
    }

    /**
     * 微信下单成功后，使用返回数据中的jsPayRequest即可调用支付
     */
    private String jsPayRequest;

    /**
     * 第三方订单号
     */
    private String targetOrderId;

    /**
     * 商户订单号
     */
    private String merOrderId;

    /**
     * 订单金额，单位分
     */
    private Integer totalAmount;

    /**
     * 状态
     */
    private String status;

    /**
     * 目标系统
     */
    private String targetSys;

    /**
     * 目标平台的状态
     */
    private String targetStatus;

    /**
     * 平台流水号
     */
    private String seqId;

    /**
     * 云闪付支付跳转url
     */
    private String redirectUrl;
}
