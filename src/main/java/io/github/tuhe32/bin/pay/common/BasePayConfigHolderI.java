package io.github.tuhe32.bin.pay.common;

/**
 * @author 刘斌
 * @date 2024/5/7 15:36
 */
public interface BasePayConfigHolderI {

    /**
     * 获取当前支付配置策略
     *
     * @return 策略名称
     */
    String get();

    /**
     * 设置当前支付配置策略
     *
     * @param label 策略名称
     */
    void set(String label);

    /**
     * 移除当前支付配置策略
     */
    void remove();

}
