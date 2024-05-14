package io.github.tuhe32.bin.pay.allin.config;

import io.github.tuhe32.bin.pay.allin.constants.AllinPayConstant;
import io.github.tuhe32.bin.pay.common.BasePayConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 刘斌
 * @date 2024/5/7 14:46
 */
@Getter
@Setter
public class AllinPayConfig extends BasePayConfig {

    private String appKey;

    private String orgId;

    private String appId;

    private String subbranch;

    private String signType;

    private String subAppId;

    private String notifyUrl;

    private String domainUrl;

    private boolean useSandboxEnv = false;

    public AllinPayConfig() {
        this.domainUrl = AllinPayConstant.SERVER_DOMAIN_PROD;
    }

    public AllinPayConfig(String appId, String appKey, String cusId) {
        this(appId, appKey, cusId, null);
    }

    public AllinPayConfig(String appId, String appKey, String cusId, String subbranch) {
        this(appId, appKey, null, cusId, subbranch);
    }

    public AllinPayConfig(String appId, String appKey, String orgId, String cusId, String subbranch) {
        this(appId, appKey, orgId, cusId, subbranch, "RSA", false);
    }

    public AllinPayConfig(String appId, String appKey, String orgId, String cusId, String subbranch, String signType, boolean useSandboxEnv) {
        super(cusId);
        this.appId = appId;
        this.appKey = appKey;
        this.orgId = orgId;
        this.subbranch = subbranch;
        this.signType = signType;
        this.domainUrl = useSandboxEnv ? AllinPayConstant.SERVER_DOMAIN_TEST : AllinPayConstant.SERVER_DOMAIN_PROD;
    }
}
