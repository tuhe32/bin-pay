package io.github.tuhe32.bin.pay.allin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/9 22:25
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class AllinPayNotify {

    /**
     * 解析回调参数
     *
     * @param callbackParams 所有回调参数
     * @return 支付回调信息
     */
    public static AllinPayNotify of(Map<String, String> callbackParams) {
        AllinPayNotify allinPayNotify = new AllinPayNotify();
        allinPayNotify.setRaw(callbackParams);
        allinPayNotify.setAppid(callbackParams.get("appid"));
        allinPayNotify.setCusid(callbackParams.get("cusid"));
        allinPayNotify.setTrxid(callbackParams.get("trxid"));
        allinPayNotify.setChnltrxid(callbackParams.get("chnltrxid"));
        allinPayNotify.setCusorderid(callbackParams.get("cusorderid"));
        allinPayNotify.setTrxcode(callbackParams.get("trxcode"));
        allinPayNotify.setTrxamt(callbackParams.get("trxamt"));
        allinPayNotify.setTrxstatus(callbackParams.get("trxstatus"));
        allinPayNotify.setTrxdate(callbackParams.get("trxdate"));
        allinPayNotify.setPaytime(callbackParams.get("paytime"));
        allinPayNotify.setTermno(callbackParams.get("termno"));
        allinPayNotify.setTermbatchid(callbackParams.get("termbatchid"));
        allinPayNotify.setTermtraceno(callbackParams.get("termtraceno"));
        allinPayNotify.setTermauthno(callbackParams.get("termauthno"));
        allinPayNotify.setTrxreserved(callbackParams.get("trxreserved"));
        allinPayNotify.setAcct(callbackParams.get("acct"));
        allinPayNotify.setInitamt(callbackParams.get("initamt"));
        allinPayNotify.setFee(callbackParams.get("fee"));
        allinPayNotify.setCmid(callbackParams.get("cmid"));
        allinPayNotify.setChnlid(callbackParams.get("chnlid"));
        allinPayNotify.setChnldata(callbackParams.get("chnldata"));
        allinPayNotify.setAccttype(callbackParams.get("accttype"));
        allinPayNotify.setBankcode(callbackParams.get("bankcode"));
        return allinPayNotify;
    }

    private Map<String, String> raw;

    private String appid;

    private String cusid;

    private String trxid;

    private String chnltrxid;

    private String cusorderid;

    private String trxcode;

    private String trxamt;

    private String trxstatus;

    private String trxdate;

    private String paytime;

    private String termno;

    private String termbatchid;

    private String termtraceno;

    private String termauthno;

    private String trxreserved;

    private String acct;

    private String initamt;

    private String fee;

    private String cmid;

    private String chnlid;

    private String chnldata;

    private String accttype;

    private String bankcode;
}
