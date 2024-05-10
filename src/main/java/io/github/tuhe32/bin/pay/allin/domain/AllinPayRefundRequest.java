package io.github.tuhe32.bin.pay.allin.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author 刘斌
 * @date 2024/5/10 11:39
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AllinPayRefundRequest extends BaseAllinPayRequest implements Serializable {

    private static final long serialVersionUID = -8786698708491355246L;

    /**
     * 商户退款订单号
     * 是否必填：是
     * 最大取值：50
     */
    @SerializedName(value = "reqsn")
    private String reqSn;

    /**
     * 原交易商户订单号
     * 是否必填：否
     */
    @SerializedName(value = "oldreqsn")
    private String oldReqSn;

    /**
     * 原交易收银宝平台流水
     * 是否必填：否
     * oldreqsn和oldtrxid必填其一，优先使用oldtrxid
     */
    @SerializedName(value = "oldtrxid")
    private String oldTrxId;

    /**
     * 退款金额
     * 是否必填：是
     */
    @SerializedName(value = "trxamt")
    private BigDecimal trxAmt;

    /**
     * 原订单总金额
     * 是否必填：否
     * 统一退款接口必传，用于判断是否部分退款
     */
    private BigDecimal orderAmount;

    /**
     * 原订单日期
     * 是否必填：否
     * 统一退款接口必传，用于判断是否当天退款
     */
    private LocalDateTime orderDate;

    /**
     * 备注
     * 是否必填：否
     * 最大取值：160（80个中文字符）
     * 禁止出现+，空格，/，?，%，#，&，=这几类特殊符号
     */
    @SerializedName(value = "remark")
    private String remark;
}
