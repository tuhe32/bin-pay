package io.github.tuhe32.bin.pay.allin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/9 17:26
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class AllinPayQuery {

    public static AllinPayQuery of(Map<String, String> raw, boolean success, boolean processing) {
        AllinPayQuery allinPayQuery = new AllinPayQuery();
        allinPayQuery.setTrxid(raw.get("trxid"));
        allinPayQuery.setTrxcode(raw.get("trxcode"));
        allinPayQuery.setTrxstatus(raw.get("trxstatus"));
        allinPayQuery.setTrxamt(raw.get("trxamt"));
        allinPayQuery.setChnltrxid(raw.get("chnltrxid"));
        allinPayQuery.setReqsn(raw.get("reqsn"));
        allinPayQuery.setAcct(raw.get("acct"));
        allinPayQuery.setFintime(raw.get("fintime"));
        allinPayQuery.setInitamt(raw.get("initamt"));
        allinPayQuery.setFee(raw.get("fee"));
        allinPayQuery.setChnlid(raw.get("chnlid"));
        allinPayQuery.setCmid(raw.get("cmid"));
        allinPayQuery.setChnldata(raw.get("chnldata"));
        allinPayQuery.setAccttype(raw.get("accttype"));
        allinPayQuery.setBankcode(raw.get("bankcode"));
        allinPayQuery.setSuccess(success);
        allinPayQuery.setProcessing(processing);
        return allinPayQuery;
    }

    private boolean success;

    private boolean processing;

    private String trxid;

    private String chnltrxid;

    private String reqsn;

    private String trxcode;

    private String trxamt;

    private String trxstatus;

    private String acct;

    private String fintime;

    private String initamt;

    private String fee;

    private String cmid;

    private String chnlid;

    private String chnldata;

    private String accttype;

    private String bankcode;

}
