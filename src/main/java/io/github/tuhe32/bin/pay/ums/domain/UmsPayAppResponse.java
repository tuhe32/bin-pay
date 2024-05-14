package io.github.tuhe32.bin.pay.ums.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/13 15:02
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class UmsPayAppResponse {

    public static UmsPayAppResponse of(Map<String, String> map) {
        UmsPayAppResponse umsPayAppResponse = new UmsPayAppResponse();
        umsPayAppResponse.setAppPayRequest(map.get("appPayRequest"));
        umsPayAppResponse.setTargetOrderId(map.get("targetOrderId"));
        umsPayAppResponse.setMerOrderId(map.get("merOrderId"));
        umsPayAppResponse.setTotalAmount(map.get("totalAmount") == null ? null :Integer.valueOf(map.get("totalAmount")));
        umsPayAppResponse.setStatus(map.get("status"));
        umsPayAppResponse.setTargetSys(map.get("targetSys"));
        umsPayAppResponse.setTargetStatus(map.get("targetStatus"));
        umsPayAppResponse.setQrCode(map.get("qrCode"));
        return umsPayAppResponse;
    }

    /**
     * 微信下单成功后，使用返回数据中的appPayRequest即可调用支付
     */
    private String appPayRequest;

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
     * 目标平台的状态
     */
    private String targetStatus;

    /**
     * 二维码
     */
    private String qrCode;
}
