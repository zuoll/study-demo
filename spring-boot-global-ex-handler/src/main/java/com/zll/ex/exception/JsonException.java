package com.zll.ex.exception;

import com.zll.ex.constants.Status;

/**
 *
 */
public class JsonException extends BaseException {


    public JsonException(Status status) {
        super(status);
    }


    public JsonException(Integer errCode, String errMsg) {
        super(errCode, errMsg);
    }
}
