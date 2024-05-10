package io.github.tuhe32.bin.pay.common.exception;

/**
 * @author 刘斌
 * @date 2024/4/26 11:27
 */
public class PayException extends Exception {

    private static final long serialVersionUID = 7688674160689392914L;

    private String errCode;
    private String errMsg;

    public PayException() {
        super();
    }


    public PayException(String message) {
        super(message);
    }

    public PayException(String message, Throwable e) {
        super(message, e);
    }

    public PayException(Throwable e) {
        super(e);
    }

    public PayException(String errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
