package io.github.tuhe32.bin.pay.allin.config;

import io.github.tuhe32.bin.pay.common.BasePayConfigHolderI;

/**
 * @author 刘斌
 * @date 2024/5/7 14:46
 */
public class AllinPayConfigHolder implements BasePayConfigHolderI {

    private static final ThreadLocal<String> THREAD_LOCAL = ThreadLocal.withInitial(() -> "default");

    /**
     * 获取当前通联支付配置策略.
     * @return 当前通联支付配置策略
     */
    @Override
    public String get() {
        return THREAD_LOCAL.get();
    }

    /**
     * 设置当前通联支付配置策略.
     * @param label 策略名称
     */
    @Override
    public void set(final String label) {
        THREAD_LOCAL.set(label);
    }

    /**
     * 此方法需要用户根据自己程序代码，在适当位置手动触发调用，本SDK里无法判断调用时机.
     */
    @Override
    public void remove() {
        THREAD_LOCAL.remove();
    }

}
