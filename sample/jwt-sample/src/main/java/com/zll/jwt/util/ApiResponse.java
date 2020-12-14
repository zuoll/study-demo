package com.zll.jwt.util;

import com.zll.jwt.exception.BaseException;
import lombok.Data;
import lombok.Getter;

/**
 * 统一的json 返回格式
 */
@Data
public class ApiResponse {

    /**
     * 返回的code
     */
    private Integer code;

    /**
     * 返回的msg
     */
    private String msg;

    /**
     * 返回的内容
     */
    private Object data;


    /**
     * 无参数构造器私有
     */
    private ApiResponse() {
    }


    /**
     * 全参数参数构造器私有
     */
    private ApiResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //===========下面的几个方法公开===========


    /**
     * 全参数的
     *
     * @param code
     * @param msg
     * @param data
     * @return
     */
    public static ApiResponse of(Integer code, String msg, Object data) {
        return new ApiResponse(code, msg, data);
    }


    /**
     * 构造一个默认的成功
     *
     * @return
     */
    public static ApiResponse ofSuccess(Object data) {
        return ofStatus(Status.OK, data);
    }


    /**
     * 构造一个带状态的
     *
     * @param status
     * @return
     */
    public static ApiResponse ofStatus(Status status) {
        return ofStatus(status, null);
    }


    /**
     * 构造一个带状态和数据
     *
     * @param status
     * @param data
     * @return
     */
    public static ApiResponse ofStatus(Status status, Object data) {
        return of(status.getCode(), status.getMsg(), data);
    }

    /**
     * 构造一个自定义成功的消息的返回
     *
     * @param msg
     * @return
     */
    public static ApiResponse ofMessage(String msg) {
        return of(Status.OK.getCode(), msg, null);

    }


    //=============异常返回============

    /**
     * 构造一个异常且带数据的返回
     *
     * @param t
     * @param data
     * @param <T>
     * @return
     */
    public static <T extends BaseException> ApiResponse ofException(T t, Object data) {
        return of(t.getErrCode(), t.getErrMsg(), data);
    }


    /**
     * 构造一个异常返回
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T extends BaseException> ApiResponse ofException(T t) {
        return ofException(t, null);
    }


    @Getter
    public enum Status {

        OK(200, "操作成功"),
        UNKNOWN_ERROR(501, "未知错误"),
        ;


        private int code;
        private String msg;

        Status(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

}
