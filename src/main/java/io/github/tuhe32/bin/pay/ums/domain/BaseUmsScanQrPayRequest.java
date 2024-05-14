package io.github.tuhe32.bin.pay.ums.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author 刘斌
 * @date 2024/5/13 15:38
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class BaseUmsScanQrPayRequest {

    /**
     * 商户号
     * 是否必填：是
     * 不传-默认从UmsPayConfig中取
     */
    @SerializedName(value = "merchantCode")
    private String merchantCode;

    /**
     * 终端号
     * 是否必填：是
     * 不传-默认从UmsPayConfig中取
     */
    @SerializedName(value = "terminalCode")
    private String terminalCode;

}
