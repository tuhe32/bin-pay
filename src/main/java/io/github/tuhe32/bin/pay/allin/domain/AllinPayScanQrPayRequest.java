package io.github.tuhe32.bin.pay.allin.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 刘斌
 * @date 2024/5/9 16:35
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AllinPayScanQrPayRequest extends BaseAllinPayRequest implements Serializable {

    private static final long serialVersionUID = -1439488049429208021L;

    /**
     * 商户的交易订单号
     * 是否必填：是
     * 最大取值：50
     */
    @SerializedName(value = "reqsn")
    private String reqSn;

    /**
     * 交易金额
     * 是否必填：是
     * 单位为分
     */
    @SerializedName(value = "trxamt")
    private BigDecimal trxAmt;

    /**
     * 扫码支付授权码
     * 是否必填：是
     */
    @SerializedName(value = "authcode")
    private String authCode;

    /**
     * 终端信息
     * 是否必填：是
     */
    private AllinPayTermInfo termInfo;

    /**
     * 订单标题
     * 是否必填：否
     * 最大取值：100（注：utf8下，一个中文字符是3个字节）
     */
    @SerializedName(value = "body")
    private String body;

    /**
     * 备注
     * 是否必填：否
     * 最大取值：160（80个中文字符）
     * 禁止出现+，空格，/，?，%，#，&，=这几类特殊符号
     */
    @SerializedName(value = "remark")
    private String remark;

    /**
     * 支付限制
     * 是否必填：否
     * no_credit--指定不能使用信用卡支付
     */
    @SerializedName(value = "limit_pay")
    private Boolean limitPay;

    @Getter
    @Setter
    public static class AllinPayTermInfo implements Serializable {

        private static final long serialVersionUID = -100588137257461248L;

        /**
         * 终端号
         * 是否必填：是
         */
        @SerializedName("termno")
        private String termNo;
        /**
         * 设备类型
         * 是否必填：否
         */
        @SerializedName("devicetype")
        private String deviceType = "11";
        /**
         * 经度
         * 是否必填：是
         * 格式：1位正负号+3位整数 +1位小数点 +5位小数
         */
        @SerializedName("longitude")
        private String longitude;
        /**
         * 纬度
         * 是否必填：是
         * 格式：1位正负号+2位整数+1位小数点 +6 位小数
         */
        @SerializedName("latitude")
        private String latitude;
        /**
         * 终端序列号
         * 是否必填：否
         */
        @SerializedName("termsn")
        private String termSn;
        /**
         * 终端IP
         * 是否必填：否
         */
        @SerializedName("deviceip")
        private String deviceIp;
    }
}
