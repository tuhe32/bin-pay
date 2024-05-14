package io.github.tuhe32.bin.pay.ums.domain;

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 刘斌
 * @date 2024/5/13 10:59
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UmsPayAppRequest extends BaseUmsNativePayRequest implements Serializable {

    private static final long serialVersionUID = 1073308703075030835L;

    /**
     * 支付金额
     * 是否必填：是
     * 单位：分
     */
    @SerializedName(value = "totalAmount")
    private Integer totalAmount;

    /**
     * 交易方式
     * 是否必填：是
     * 微信：WXPAY，支付宝：ALIPAY, 云闪付：UACPAY
     */
    private transient String payType;

    /**
     * 商户订单号
     * 是否必填：否，不填按照默认规则生成
     * 长度：6-32
     */
    @SerializedName(value = "merOrderId")
    private String merOrderId;

    /**
     * 支付限制
     * 是否必填：否
     * 是否需要限制信用卡支付
     */
    @SerializedName(value = "limitCreditCard")
    private boolean limitCreditCard;

    /**
     * 平台商户分账金额
     * 是否必填：否
     */
    @SerializedName(value = "platformAmount")
    private Integer platformAmount;

    /**
     * 微信子appid，微信小程序必传，默认从UmsPayConfig中取
     * 是否必填：否
     */
    @SerializedName(value = "subAppId")
    private String subAppId;

    /**
     * 过期时间
     * 是否必填：否
     * 为空则使用系统默认过期时间（30分钟），格式yyyy-MM-dd HH:mm:ss
     */
    @SerializedName(value = "expireTime")
    private String expireTime;

    /**
     * 订单描述
     * 是否必填：否
     * 255以内
     */
    @SerializedName(value = "orderDesc")
    private String orderDesc;

    /**
     * 请求时间戳
     * 是否必填：不填
     * 格式yyyy-MM-dd HH:mm:ss
     */
    @SerializedName(value = "requestTimestamp")
    private String requestTimestamp;

    /**
     * 机构商户号
     * 是否必填：不填
     */
    @SerializedName(value = "instMid")
    private String instMid;

    /**
     * 交易类型
     * 是否必填：不填
     */
    @SerializedName(value = "tradeType")
    private String tradeType;

    /**
     * 子订单信息
     * 是否必填：不填
     */
    @SerializedName(value = "subOrders")
    private String subOrders;


    public UmsPayAppRequest setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = BaseWxPayRequest.yuan2Fen(totalAmount);
        return this;
    }

    @Override
    public UmsPayAppRequest setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }


}
