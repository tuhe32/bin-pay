package io.github.tuhe32.bin.pay.ums.config;

import io.github.tuhe32.bin.pay.common.BasePayConfig;
import io.github.tuhe32.bin.pay.ums.constants.UmsPayConstants;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 刘斌
 * @date 2024/5/11 15:40
 */
@Getter
@Setter
public class UmsPayConfig extends BasePayConfig {

    private String appId;

    private String appKey;

    private String parentMid;

    private String tid;

    private String md5Key;

    private String sysCodePrefix;

    private String subAppId;

    private Boolean divisionFlag;

    private String notifyUrl;

    private String domainUrl;

    private boolean useSandboxEnv = false;

    public UmsPayConfig() {
        this.domainUrl = UmsPayConstants.SERVER_DOMAIN_PROD;
    }

    public UmsPayConfig(String appId, String appKey, String mid, String tid, String sysCodePrefix, String md5Key) {
        this(appId, appKey, mid, tid, sysCodePrefix, md5Key, null);
    }

    public UmsPayConfig(String appId, String appKey, String mid, String tid, String sysCodePrefix, String md5Key, String notifyUrl) {
        this(appId, appKey, mid, tid, sysCodePrefix, md5Key, notifyUrl, false);
    }

    public UmsPayConfig(String appId, String appKey, String mid, String tid, String sysCodePrefix, String md5Key, String notifyUrl, boolean useSandboxEnv) {
        super(mid);
        this.appId = appId;
        this.appKey = appKey;
        this.md5Key = md5Key;
        this.tid = tid;
        this.sysCodePrefix = sysCodePrefix;
        this.notifyUrl = notifyUrl;
        this.domainUrl = useSandboxEnv ? UmsPayConstants.SERVER_DOMAIN_TEST : UmsPayConstants.SERVER_DOMAIN_PROD;
    }

}
