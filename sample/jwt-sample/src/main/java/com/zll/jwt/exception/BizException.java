package com.zll.jwt.exception;

import lombok.Getter;
import lombok.Setter;

public class BizException extends BaseException {


    /**
     *
     */
    private static final long serialVersionUID = 7907483823714463305L;
    /**
     *
     */
    @Setter
    @Getter
    private Integer errcode;
    /**
     *
     */
    @Getter
    @Setter
    private String errmsg;

    public BizException(String errmsg) {
        this(null, errmsg);
    }

    public BizException(Integer errcode, String errmsg) {
        super(errcode, errmsg);
        this.errcode = errcode;
        this.errmsg = errmsg;
    }
}
