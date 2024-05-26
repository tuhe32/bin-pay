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
 * @date 2024/5/14 10:19
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UmsPayRefundRequest extends BaseUmsPayRequest implements Serializable {

    private static final long serialVersionUID = 7949211493489559447L;

    /**
     * 退货金额
     * 是否必填：是
     * 单位：分
     */
    @SerializedName(value = "refundAmount")
    private Integer refundAmount;

    /**
     * 商户订单号
     * 是否必填：是
     * 长度：6-32
     */
    @SerializedName(value = "merOrderId")
    private String merOrderId;

    /**
     * 退款订单号
     * 是否必填：否。多次退款必传，每次退款上送的
     * refundOrderId值需不同
     */
    @SerializedName(value = "refundOrderId")
    private String refundOrderId;

    /**
     * 平台商户分账金额
     * 是否必填：否
     */
    @SerializedName(value = "platformAmount")
    private Integer platformAmount;

    /**
     * 机构商户号
     * 是否必填：不填
     */
    @SerializedName(value = "instMid")
    private String instMid;

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

    public UmsPayRefundRequest setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = BaseWxPayRequest.yuan2Fen(refundAmount);
        return this;
    }

    public UmsPayRefundRequest setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
        return this;
    }

}
