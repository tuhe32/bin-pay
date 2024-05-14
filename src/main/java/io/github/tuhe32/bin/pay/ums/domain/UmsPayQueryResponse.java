package io.github.tuhe32.bin.pay.ums.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/14 09:29
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class UmsPayQueryResponse {

    public static UmsPayQueryResponse of(Map<String, String> map) {
        UmsPayQueryResponse umsPayQueryResponse = new UmsPayQueryResponse();
        umsPayQueryResponse.setStatus(map.get("status"));
        umsPayQueryResponse.setTotalAmount(map.get("totalAmount") == null ? null :Integer.valueOf(map.get("totalAmount")));
        umsPayQueryResponse.setMerOrderId(map.get("merOrderId") == null ? null :Integer.valueOf(map.get("merOrderId")));
        umsPayQueryResponse.setTargetOrderId(map.get("targetOrderId"));
        umsPayQueryResponse.setTargetSys(map.get("targetSys"));
        umsPayQueryResponse.setTargetStatus(map.get("targetStatus"));
        umsPayQueryResponse.setBillFunds(map.get("billFunds"));
        umsPayQueryResponse.setPayTime(map.get("payTime"));
        umsPayQueryResponse.setSettleDate(map.get("settleDate"));
        umsPayQueryResponse.setBuyerPayAmount(map.get("buyerPayAmount"));
        return umsPayQueryResponse;
    }

    private String status;

    private Integer totalAmount;

    private Integer merOrderId;

    private String targetOrderId;

    private String targetSys;

    private String targetStatus;

    private String billFunds;

    private String payTime;

    private String settleDate;

    private String buyerPayAmount;


}
