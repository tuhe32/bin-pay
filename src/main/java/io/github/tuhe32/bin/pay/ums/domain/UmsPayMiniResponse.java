package io.github.tuhe32.bin.pay.ums.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/13 10:10
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class UmsPayMiniResponse {

    public static UmsPayMiniResponse of(Map<String, String> map) {
        UmsPayMiniResponse umsPayMiniResponse = new UmsPayMiniResponse();
        umsPayMiniResponse.setMiniPayRequest(map.get("miniPayRequest"));
        umsPayMiniResponse.setTargetOrderId(map.get("targetOrderId"));
        umsPayMiniResponse.setMerOrderId(map.get("merOrderId"));
        umsPayMiniResponse.setTotalAmount(map.get("totalAmount") == null ? null :Integer.valueOf(map.get("totalAmount")));
        umsPayMiniResponse.setStatus(map.get("status"));
        umsPayMiniResponse.setTargetSys(map.get("targetSys"));
        umsPayMiniResponse.setQrCode(map.get("qrCode"));
        return umsPayMiniResponse;
    }

    /**
     * 微信下单成功后，使用返回数据中的miniPayRequest即可调用支付
     */
    private String miniPayRequest;

    /**
     * 支付宝下单成功后，使用返回数据中的targetOrderId即可调用支付
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
     * 二维码
     */
    private String qrCode;

}
