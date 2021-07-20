package com.wenxia.miaosha.base.exception;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-13
 */
public class LockFailException extends RuntimeException {

    public LockFailException(String message) {
        super(message);
    }
}
