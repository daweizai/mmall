package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 全局返回数据封装类
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private static final long serialVersionUID = 4095343419963698811L;

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    /*成功时使用*/
    public static <T> ServerResponse<T> success() {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> successByMsg(String msg) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> success(T data, String msg) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> success(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    /*错误时使用*/
    public static <T> ServerResponse<T> error() {
        return new ServerResponse<>(ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> error(String msg) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), msg);
    }

    public static <T> ServerResponse<T> error(ResponseCode responseCode) {
        return new ServerResponse<>(responseCode.getCode(), responseCode.getDesc());
    }

    public static <T> ServerResponse<T> error(int errorCode, String errorMsg) {
        return new ServerResponse<>(errorCode, errorMsg);
    }

}
