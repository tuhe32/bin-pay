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
 * @date 2024/5/9 16:21
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AllinPayUnitOrderRequest extends BaseAllinPayRequest implements Serializable {

    private static final long serialVersionUID = 140830451038929091L;

    /**
     * 交易方式
     * 是否必填：是
     */
    @SerializedName(value = "paytype")
    private String payType;

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
     * 订单标题
     * 是否必填：否
     * 最大取值：100（注：utf8下，一个中文字符是3个字节）
     */
    @SerializedName(value = "body")
    private String body;

    /**
     * 支付平台用户标识
     * 是否必填：否
     * 微信支付-用户的微信openid
     * 支付宝支付-用户user_id
     * 微信小程序-用户小程序的openid
     * 云闪付JS-用户userId
     */
    @SerializedName(value = "acct")
    private String subOpenId;

    /**
     * 微信子appid
     * 是否必填：否
     */
    @SerializedName(value = "sub_appid")
    private String subAppId;

    /**
     * 备注
     * 是否必填：否
     * 最大取值：160（80个中文字符）
     * 禁止出现+，空格，/，?，%，#，&，=这几类特殊符号
     */
    @SerializedName(value = "remark")
    private String remark;

    /**
     * 有效时间
     * 是否必填：否
     * 以分钟为单位，不填默认为5分钟
     */
    @SerializedName(value = "validtime")
    private Integer validTime;

    /**
     * 支付限制
     * 是否必填：否
     * no_credit--指定不能使用信用卡支付
     */
    @SerializedName(value = "limit_pay")
    private Boolean limitPay;
}
