package io.github.tuhe32.bin.pay.common.utils;

import io.github.tuhe32.bin.pay.allin.constants.AuthCodeConstant;

/**
 * @author 刘斌
 * @date 2024/5/9 11:36
 */
public class PayUtils {

    /**
     * 支付授权码
     * 微信:付款码条形码规则：18位纯数字，以10、11、12、13、14、15开头
     * 支付宝:25~30开头的长度为16~24位的数字，实际字符串长度以开发者获取的付款码长度为准
     * 银联:62、63、68开头的19位码，实际字符串长度以开发者获取的付款码长度为准
     *
     * @param authCode 支付授权码
     * @return 1:微信 2:支付宝 3:银联
     */
    public static int checkAuthCode(String authCode) {

        int wxMin = 18;
        int wxMax = 18;
        int aliMin = 16;
        int aliMax = 24;
        int unionMin = 19;
        int unionMax = 19;
        if (authCode == null || authCode.length() < 1) {
            throw new RuntimeException("支付授权码为空!");
        }
        String wxPayStartStr = "10,11,12,13,14,15";
        String aliPayStartStr = "25,26,27,28,29,30";
        String unionPayStartStr = "62,63,68";

        String[] wxPayStarts = wxPayStartStr.split(",");
        String[] aliPayStarts = aliPayStartStr.split(",");
        String[] unionPayStarts = unionPayStartStr.split(",");
        String substring = authCode.substring(0, 2);
        if (authCode.length() >= wxMin && authCode.length() <= wxMax) {
            for (String str : wxPayStarts) {
                if (str.equals(substring)) {
                    return AuthCodeConstant.AUTH_CODE_WEIXIN;
                }
            }
        }
        if (authCode.length() >= aliMin && authCode.length() <= aliMax) {
            for (String str : aliPayStarts) {
                if (str.equals(substring)) {
                    return AuthCodeConstant.AUTH_CODE_ALI;
                }
            }
        }
        if (authCode.length() >= unionMin && authCode.length() <= unionMax) {
            for (String str : unionPayStarts) {
                if (str.equals(substring)) {
                    return AuthCodeConstant.AUTH_CODE_UNION;
                }
            }
        }
        throw new RuntimeException("支付授权码格式错误，【授权码】：" + authCode);
    }

}
