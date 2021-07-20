package com.wenxia.miaosha.base;


import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-11
 */
@Getter
@Setter
public class OneResponse<T> {

    private int code;
    private String message;
    private T data;

    public OneResponse() {
        this(0, "success");
    }

    public OneResponse(T data) {
        this(0, "success", data);
    }

    public OneResponse(int code, String message) {
        this(code, message, null);
    }

    public OneResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return this.code == 0;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
