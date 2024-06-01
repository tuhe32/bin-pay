package io.github.tuhe32.bin.pay.allin.constants;

/**
 * @author 刘斌
 * @date 2024/5/8 16:40
 */
public class AllinPayConstant {

    public static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCm9OV6zH5DYH/ZnAVYHscEELdCNfNTHGuBv1nYYEY9FrOzE0/4kLl9f7Y9dkWHlc2ocDwbrFSm0Vqz0q2rJPxXUYBCQl5yW3jzuKSXif7q1yOwkFVtJXvuhf5WRy+1X5FOFoMvS7538No0RpnLzmNi3ktmiqmhpcY/1pmt20FHQQIDAQAB";
    public static final String SIGN_TYPE = "RSA";

    public static final String SERVER_DOMAIN_PROD = "https://vsp.allinpay.com";
    public static final String H5_SERVER_DOMAIN_PROD = "https://syb.allinpay.com";
    public static final String SERVER_DOMAIN_TEST = "https://syb-test.allinpay.com";

    public static final String UNIT_ORDER = "/apiweb/unitorder/pay";
    public static final String SCAN_QR_PAY = "/apiweb/unitorder/scanqrpay";
    public static final String QUERY_ORDER = "/apiweb/tranx/query";
    public static final String REFUND_ORDER = "/apiweb/tranx/refund";
    public static final String CANCEL_ORDER = "/apiweb/tranx/cancel";
    public static final String ADD_TERM = "/cusapi/merchantapi/addterm";
    public static final String H5_CHECKOUT_PAY = "/apiweb/h5unionpay/unionorder";


    /**
     * 交易状态：字段trxstatus
     */
    public static final String TRX_STATUS = "trxstatus";
    /**
     * 查询支付成功返回trxStatus
     */
    public static final String STATUS_SUCCESS = "0000";
    /**
     * 交易处理中
     */
    public static final String STATUS_WAIT = "2000";

    /**
     * 调用成功返回retcode
     */
    public static final String CODE_SUCCESS = "SUCCESS";

    /**
     * 查询间隔时间，单位：秒，默认10秒
     */
    public static final long CHECK_INTERVAL = 10;
    /**
     * 超时时限，单位：秒，默认50秒
     */
    public static final long TIMEOUT = 50;
}
