package io.github.tuhe32.bin.pay.ali.config;

/**
 * @author 刘斌
 * @date 2024/4/26 11:14
 * 支付宝-支付配置策略
 */
public class AliPayConfigHolder {

    private static final ThreadLocal<String> THREAD_LOCAL = ThreadLocal.withInitial(() -> "default");

    /**
     * 获取当前支付宝支付配置策略.
     * @return 当前支付宝支付配置策略
     */
    public static String get() {
        return THREAD_LOCAL.get();
    }

    /**
     * 设置当前支付宝支付配置策略.
     * @param label 策略名称
     */
    public static void set(final String label) {
        THREAD_LOCAL.set(label);
    }

    /**
     * 此方法需要用户根据自己程序代码，在适当位置手动触发调用，本SDK里无法判断调用时机.
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
    
}
