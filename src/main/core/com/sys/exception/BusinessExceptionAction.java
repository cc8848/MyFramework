package com.sys.exception;

/**
 * @author zzl
 *         Date:2014-07-28
 */
public class BusinessExceptionAction extends RuntimeException {
    public BusinessExceptionAction(String message) {
        super(message);
    }

    public BusinessExceptionAction(Throwable cause) {
        super(cause);
    }
    public BusinessExceptionAction(String message, Throwable cause)
    {
        super(message,cause);
    }
}
