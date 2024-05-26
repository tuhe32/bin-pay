package io.github.tuhe32.bin.pay.common.exception;

/**
 * @author 刘斌
 * @date 2024/5/24 16:02
 */
@FunctionalInterface
public interface CheckedSupplier<R, E extends Exception> {

    /**
     * 自定义支持受检异常的Supplier接口
     *
     * @return R
     * @throws E 可抛出的异常
     */
    R get() throws E;
}
