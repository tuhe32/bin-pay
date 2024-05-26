package io.github.tuhe32.bin.pay.ums.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 刘斌
 * @date 2024/5/14 09:16
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UmsPayQueryRequest extends BaseUmsPayRequest implements Serializable {

    private static final long serialVersionUID = -6449270424715335951L;

    /**
     * 商户订单号
     * 是否必填：是
     * 长度：6-32
     */
    @SerializedName(value = "merOrderId")
    private String merOrderId;

    /**
     * 交易方式
     * 是否必填：否（APP支付的查询必填）
     * 微信：WXPAY，支付宝：ALIPAY, 云闪付：UACPAY
     */
    private transient String payType;

    /**
     * 机构商户号
     * 是否必填：不填（默认生成）
     */
    @SerializedName(value = "instMid")
    private String instMid;

    /**
     * 请求时间戳
     * 是否必填：不填（默认生成）
     * 格式yyyy-MM-dd HH:mm:ss
     */
    @SerializedName(value = "requestTimestamp")
    private String requestTimestamp;
}
