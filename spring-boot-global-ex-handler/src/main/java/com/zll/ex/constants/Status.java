package com.zll.ex.constants;

import lombok.Getter;

/**
 * 状态码
 */
@Getter
public enum  Status {

    OK(200, "操作成功"),
    UNKNOWN_ERROR(501, "未知错误"),
    ;


    private int code;
    private String msg;

    Status(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
