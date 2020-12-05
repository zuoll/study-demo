package com.zll.ex.exception;

import com.zll.ex.constants.Status;

/**
 *
 */
public class PageException extends BaseException {


    public PageException(Status status) {
        super(status);
    }


    public PageException(Integer errCode, String errMsg) {
        super(errCode, errMsg);
    }
}
