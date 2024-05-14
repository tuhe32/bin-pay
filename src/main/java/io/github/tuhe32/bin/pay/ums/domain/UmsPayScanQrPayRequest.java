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
 * @date 2024/5/13 15:35
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UmsPayScanQrPayRequest extends BaseUmsScanQrPayRequest implements Serializable {

    private static final long serialVersionUID = -8300508707936761521L;

    /**
     * 支付金额
     * 是否必填：是
     * 单位：分
     */
    @SerializedName(value = "transactionAmount")
    private Integer transactionAmount;

    /**
     * 支付码
     * 是否必填：是
     */
    @SerializedName(value = "payCode")
    private String payCode;

    /**
     * 备注
     * 是否必填：否
     * 255以内
     */
    @SerializedName(value = "merchantRemark")
    private String merchantRemark;

    /**
     * 支付限制
     * 是否必填：否
     * 是否需要限制信用卡支付
     */
    @SerializedName(value = "limitCreditCard")
    private boolean limitCreditCard;

    /**
     * 微信子appid，微信小程序必传
     * 是否必填：否
     */
    @SerializedName(value = "subAppId")
    private String subAppId;

    /**
     * 交易币种
     * 是否必填：不填
     */
    @SerializedName(value = "transactionAmount")
    private String transactionCurrencyCode;

    /**
     * 支付方式
     * 是否必填：不填
     */
    @SerializedName(value = "payMode")
    private String payMode;

    /**
     * 设备类型
     * 是否必填：不填
     */
    @SerializedName(value = "deviceType")
    private String deviceType;

    /**
     * 终端硬件序列号
     * 是否必填：不填
     */
    @SerializedName(value = "serialNum")
    private String serialNum;

    /**
     * 商户订单号
     * 是否必填：不填
     * 长度：6-32
     */
    @SerializedName(value = "merchantOrderId")
    private String merchantOrderId;

    public UmsPayScanQrPayRequest setTransactionAmount(BigDecimal totalAmount) {
        this.transactionAmount = BaseWxPayRequest.yuan2Fen(totalAmount);
        return this;
    }

    public UmsPayScanQrPayRequest setTotalAmount(Integer totalAmount) {
        this.transactionAmount = totalAmount;
        return this;
    }

}
