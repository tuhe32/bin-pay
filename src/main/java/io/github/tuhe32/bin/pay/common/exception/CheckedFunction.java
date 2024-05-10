package io.github.tuhe32.bin.pay.common.exception;

/**
 * @author 刘斌
 * @date 2024/4/27 11:06
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {

    /**
     * 自定义支持受检异常的Function接口
     *
     * @param t 参数
     * @return 返回
     * @throws E 可抛出的异常
     */
    R apply(T t) throws E;
}
