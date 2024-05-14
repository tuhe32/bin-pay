package io.github.tuhe32.bin.pay.ums.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author 刘斌
 * @date 2024/5/11 17:58
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class BaseUmsPayRequest {

    /**
     * 商户号
     * 是否必填：是
     * 不传-默认从UmsPayConfig中取
     */
    @SerializedName(value = "mid")
    private String mid;

    /**
     * 终端号
     * 是否必填：是
     * 不传-默认从UmsPayConfig中取
     */
    @SerializedName(value = "tid")
    private String tid;

    /**
     * 签名类型
     * 是否必填：是
     * 不传-默认从UmsPayConfig中取
     */
    @SerializedName(value = "signType")
    private String signType;

    /**
     * 交易结果通知地址
     * 是否必填：否
     * 不传-默认从UmsPayConfig中取
     */
    @SerializedName(value = "notify_url")
    private String notifyUrl;
}
