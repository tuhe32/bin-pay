package io.github.tuhe32.bin.pay.ums.constants;

/**
 * @author 刘斌
 * @date 2024/5/11 15:41
 */
public class UmsPayConstants {

    public static final String SIGN_TYPE = "SHA256";

    public static final String SERVER_DOMAIN_PROD = "https://api-mop.chinaums.com";
    public static final String SERVER_DOMAIN_TEST = "https://test-api-open.chinaums.com";

    public static final String MINI_WEIXIN_ORDER = "/v1/netpay/wx/unified-order";
    public static final String MINI_ALI_ORDER = "/v1/netpay/trade/create";
    public static final String MINI_UAC_ORDER = "/v1/netpay/uac/mini-order";

    public static final String APP_WEIXIN_ORDER = "/v1/netpay/wx/app-pre-order";
    public static final String APP_ALI_ORDER = "/v1/netpay/trade/precreate";
    public static final String APP_UAC_ORDER = "/v1/netpay/uac/app-order";

    public static final String H5_WEXIN_ORDER = "/v1/netpay/webpay/pay";
    public static final String JS_ACP_ORDER = "/v1/netpay/acp/js-pay";

    public static final String SCAN_QR_ORDER = "/v4/poslink/transaction/pay";

    public static final String MINI_QUERY = "/v1/netpay/query";
    public static final String APP_WEIXIN_QUERY = "/v1/netpay/wx/app-pre-query";
    public static final String APP_ALI_QUERY = "/v1/netpay/trade/app-pre-query";

    public static final String REFUND_ORDER = "/v1/netpay/refund";
    public static final String SCAN_QR_REFUND_ORDER = "/v2/poslink/transaction/refund";


    /**
     * 调用成功返回retcode
     */
    public static final String CODE_SUCCESS = "SUCCESS";
    /**
     * 查询支付成功返回trxStatus
     */
    public static final String STATUS_SUCCESS = "TRADE_SUCCESS";
    public static final String STATUS_CLOSED = "TRADE_CLOSED";


    public static final String PAY_TYPE_WXPAY = "WXPAY";

    public static final String PAY_TYPE_ALIPAY = "ALIPAY";

    public static final String PAY_TYPE_UACPAY = "UACPAY";




}
