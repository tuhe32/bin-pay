package io.github.tuhe32.bin.pay.allin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/7 20:40
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class AllinPayExtraData {

    public static AllinPayExtraData of(Map<String, String> params) {
        AllinPayExtraData extraData = new AllinPayExtraData();
        if (params == null) {
            return extraData;
        }
        extraData.setReqsn(params.get("reqsn"));
        extraData.setTrxamt(params.get("trxamt"));
        extraData.setBody(params.get("body"));
        extraData.setIsdirectpay(params.get("isdirectpay"));
        extraData.setVersion(params.get("version"));
        extraData.setRemark(params.get("remark"));
        extraData.setValidtime(params.get("validtime"));
        extraData.setLimit_pay(params.get("limit_pay"));
        extraData.setSubbranch(params.get("subbranch"));
        extraData.setSign(params.get("sign"));
        extraData.setSigntype(params.get("signtype"));
        extraData.setOrgid(params.get("orgid"));
        extraData.setCusid(params.get("cusid"));
        extraData.setAppid(params.get("appid"));
        extraData.setNotify_url(params.get("notify_url"));
        extraData.setRandomstr(params.get("randomstr"));
        extraData.setPaytype(params.get("paytype"));

        return extraData;
    }

    private String reqsn;
    private String trxamt;
    private String body;
    private String isdirectpay;
    private String version;
    private String remark;
    private String validtime;
    private String limit_pay;
    private String subbranch;
    private String sign;
    private String signtype;
    private String orgid;
    private String cusid;
    private String appid;
    private String notify_url;
    private String randomstr;
    private String paytype;
}
