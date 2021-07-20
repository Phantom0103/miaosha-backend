package com.wenxia.miaosha.base.exception;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-06-29
 */
public class CustomMQClientException extends RuntimeException {

    public CustomMQClientException(String message) {
        super(message);
    }

    public CustomMQClientException(Throwable cause) {
        super(cause);
    }

    public CustomMQClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
