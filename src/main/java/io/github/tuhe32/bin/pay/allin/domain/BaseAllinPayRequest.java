package io.github.tuhe32.bin.pay.allin.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/9 14:49
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class BaseAllinPayRequest {

    /**
     * 集团/代理商商户号
     * 是否必填：否
     * 不传-默认从AllinPayConfig中取
     */
    @SerializedName(value = "orgid")
    private String orgId;

    /**
     * 应用ID
     * 是否必填：否
     * 不传-默认从AllinPayConfig中取
     */
    @SerializedName(value = "appid")
    private String appId;

    /**
     * 商户号
     * 是否必填：否
     * 不传-默认从AllinPayConfig中取
     */
    @SerializedName(value = "cusid")
    private String cusId;

    /**
     * 门店号
     * 是否必填：否
     * 不传-默认从AllinPayConfig中取
     */
    @SerializedName(value = "subbranch")
    private String subbranch;

    /**
     * 交易结果通知地址
     * 是否必填：否
     * 不传-默认从AllinPayConfig中取
     */
    @SerializedName(value = "notify_url")
    private String notifyUrl;

    /**
     * 拓展参数，常用参数之外的参数
     * 是否必填：否
     */
    private Map<String, String> extraData;

}
