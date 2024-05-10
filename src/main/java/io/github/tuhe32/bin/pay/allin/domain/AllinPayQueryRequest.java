package io.github.tuhe32.bin.pay.allin.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/9 17:15
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AllinPayQueryRequest extends BaseAllinPayRequest implements Serializable {

    private static final long serialVersionUID = 1910254147404540124L;

    /**
     * 商户的交易订单号
     * 是否必填：是
     * 最大取值：50
     */
    @SerializedName(value = "reqsn")
    private String reqSn;

    /**
     * 通联平台交易流水
     * 是否必填：否
     */
    @SerializedName(value = "trxid")
    private String trxId;

    public AllinPayQueryRequest(Map<String, String> map) {
        this.setReqSn(map.get("reqsn"));
        this.setOrgId(map.get("orgid"));
        this.setAppId(map.get("appid"));
        this.setCusId(map.get("cusid"));
        this.setSubbranch(map.get("subbranch"));

    }
}
