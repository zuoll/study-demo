package com.zll.jwt.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    private Integer errCode;

    private String errMsg;

    public BaseException(String errMsg) {
        //这个必须要调用一下，否则堆栈抛出的异常是null
        super(errMsg);
        this.errMsg = errMsg;
    }

    public BaseException(Integer errCode, String errMsg) {
        //这个必须要调用一下，否则堆栈抛出的异常是null
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }


}
