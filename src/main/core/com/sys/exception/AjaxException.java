package com.sys.exception;

/**
 * Created by lenovo on 15-1-7.
 */
public class AjaxException extends  RuntimeException{
    public AjaxException() {
    }

    public AjaxException(String message) {
        super(message);
    }
}
