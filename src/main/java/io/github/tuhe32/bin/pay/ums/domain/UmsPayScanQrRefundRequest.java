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
 * @date 2024/5/14 11:40
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UmsPayScanQrRefundRequest extends BaseUmsScanQrPayRequest implements Serializable {

    private static final long serialVersionUID = -8916038029598460002L;

    /**
     * 商户订单号
     * 是否必填：必填
     * 长度：6-32
     */
    @SerializedName(value = "merchantOrderId")
    private String merchantOrderId;

    /**
     * 退款金额
     * 是否必填：必填
     * 单位：分
     */
    @SerializedName(value = "transactionAmount")
    private Integer transactionAmount;

    /**
     * 退款请求标识
     * 是否必填：必填
     * 标识一次退款请求，同一笔订单多次退款需要保证唯一，长度不超过50位
     */
    @SerializedName(value = "refundRequestId")
    private String refundRequestId;

    /**
     * 备注
     * 是否必填：否
     * 255以内
     */
    @SerializedName(value = "merchantRemark")
    private String merchantRemark;

    /**
     * 退款说明
     * 是否必填：否
     */
    @SerializedName(value = "refundDesc")
    private String refundDesc;

    public UmsPayScanQrRefundRequest setTransactionAmount(BigDecimal totalAmount) {
        this.transactionAmount = BaseWxPayRequest.yuan2Fen(totalAmount);
        return this;
    }

    public UmsPayScanQrRefundRequest setTotalAmount(Integer totalAmount) {
        this.transactionAmount = totalAmount;
        return this;
    }

}
