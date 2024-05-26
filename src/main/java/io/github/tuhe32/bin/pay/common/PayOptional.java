package io.github.tuhe32.bin.pay.common;


import io.github.tuhe32.bin.pay.common.exception.CheckedSupplier;
import io.github.tuhe32.bin.pay.common.exception.PayException;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author 刘斌
 * @date 2024/5/24 22:31
 */
public class PayOptional<T> {

    private final T t;

    private final PayException payException;

    public PayOptional(T t, PayException payException) {
        this.t = t;
        this.payException = payException;
    }

    public static <T> PayOptional<T> of(CheckedSupplier<T, PayException> checkedSupplier) {
        Objects.requireNonNull(checkedSupplier);
        try {
            T t = checkedSupplier.get();
            return new PayOptional<T>(t, null);
        } catch (PayException e) {
            return new PayOptional<T>(null, e);
        }
    }

    public PayOptional<T> success(Consumer<T> successConsumer) {
        Objects.requireNonNull(successConsumer);
        if (t != null) {
            successConsumer.accept(t);
        }
        return this;
    }

    public <R> PayOptional<R> success(Function<T, R> successFunction) {
        Objects.requireNonNull(successFunction);
        if (t != null) {
            R r = successFunction.apply(t);
            return new PayOptional<>(r, null);
        }
        return new PayOptional<>(null, payException);
    }

    public PayOptional<T> fail(Consumer<PayException> failConsumer) {
        Objects.requireNonNull(failConsumer);
        if (payException != null) {
            failConsumer.accept(payException);
        }
        return this;
    }

    public T get() {
        return t;
    }

}
