package io.github.tuhe32.bin.pay.ums.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 刘斌
 * @date 2024/5/14 09:35
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class BaseUmsNativePayRequest extends BaseUmsPayRequest implements Serializable {

    private static final long serialVersionUID = 7011046721692229160L;

    /**
     * 支付金额
     * 是否必填：是
     * 单位：分
     */
    @SerializedName(value = "totalAmount")
    private Integer totalAmount;

    /**
     * 商户订单号
     * 是否必填：否，不填按照默认规则生成
     * 长度：6-32
     */
    @SerializedName(value = "merOrderId")
    private String merOrderId;

    /**
     * 平台商户分账金额
     * 是否必填：否
     */
    @SerializedName(value = "platformAmount")
    private Integer platformAmount;

    /**
     * 微信子appid，微信小程序必传
     * 是否必填：否
     */
    @SerializedName(value = "subAppId")
    private String subAppId;

    /**
     * 交易方式
     * 是否必填：是
     * 微信：WXPAY，支付宝：ALIPAY, 云闪付：UACPAY
     */
    private transient String payType;

    /**
     * 请求时间戳
     * 是否必填：不填
     * 格式yyyy-MM-dd HH:mm:ss
     */
    @SerializedName(value = "requestTimestamp")
    private String requestTimestamp;

    /**
     * 子订单信息
     * 是否必填：不填
     */
    @SerializedName(value = "subOrders")
    private String subOrders;

}
