package io.github.tuhe32.bin.pay.ali.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import io.github.tuhe32.bin.pay.ali.constants.AliPayConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 刘斌
 * @date 2024/4/26 11:18
 */
@Getter
@Setter
public class AliPayConfig {

    private String serverUrl;

    private String appId;

    private String privateKey;

    private String format;

    private String charset;

    private String alipayPublicKey;

    private String signType;

    private AlipayClient client;

    private String returnUrl;

    private String notifyUrl;

    private boolean useSandboxEnv = false;

    public AliPayConfig(String appId, String privateKey, String alipayPublicKey) {
        this(appId, privateKey, "json", "utf-8", alipayPublicKey, AliPayConstant.SIGN_TYPE, false);
    }

    public AliPayConfig(String appId, String privateKey, String alipayPublicKey, String signType) {
        this(appId, privateKey, "json", "utf-8", alipayPublicKey, signType, false);
    }

    public AliPayConfig(String appId, String privateKey, String format, String charset,
                  String alipayPublicKey, String signType, boolean useSandboxEnv) {
        this.serverUrl = useSandboxEnv ? AliPayConstant.SERVER_URL_DEV : AliPayConstant.SERVER_URL_PROD;
        this.appId = appId;
        this.privateKey = privateKey;
        this.format = format;
        this.charset = charset;
        this.alipayPublicKey = alipayPublicKey;
        this.signType = signType;
        this.client = new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);
    }
}
