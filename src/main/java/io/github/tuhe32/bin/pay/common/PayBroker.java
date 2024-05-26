package io.github.tuhe32.bin.pay.common;

import io.github.tuhe32.bin.pay.common.exception.CheckedSupplier;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author 刘斌
 * @date 2024/5/24 15:54
 */
public class PayBroker {

    public static <R> void broker(CheckedSupplier<R, PayException> checkedSupplier, Consumer<R> success, Consumer<PayException> fail) {
        Objects.requireNonNull(checkedSupplier);
        Objects.requireNonNull(success);
        Objects.requireNonNull(fail);
        try {
            R r = checkedSupplier.get();
            success.accept(r);
        } catch (PayException e) {
            fail.accept(e);
        }
    }

    public static <R, T> T broker(CheckedSupplier<R, PayException> checkedSupplier, Function<R, T> success, Consumer<PayException> fail) {
        Objects.requireNonNull(checkedSupplier);
        Objects.requireNonNull(success);
        Objects.requireNonNull(fail);
        try {
            R r = checkedSupplier.get();
            return success.apply(r);
        } catch (PayException e) {
            fail.accept(e);
        }
        return null;
    }

    public static <R, T> T broker(CheckedSupplier<R, PayException> checkedSupplier, Function<R, T> success, Function<PayException, T> fail) {
        Objects.requireNonNull(checkedSupplier);
        Objects.requireNonNull(success);
        Objects.requireNonNull(fail);
        try {
            R r = checkedSupplier.get();
            return success.apply(r);
        } catch (PayException e) {
            return fail.apply(e);
        }
    }

    public static <T> Pair<T, PayException> broker(CheckedSupplier<T, PayException> checkedSupplier) {
        Objects.requireNonNull(checkedSupplier);
        try {
            T t = checkedSupplier.get();
            return Pair.of(t, null);
        } catch (PayException e) {
            return Pair.of(null, e);
        }
    }

}
