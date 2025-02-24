package com.shop.exception;

import com.shop.constant.enums.ErrorCode;
import com.shop.constant.interfaces.ExceptionResult;
import lombok.Getter;

@Getter
public class ExceptionNotMatched extends RuntimeException implements ExceptionResult {

    final int errorCode;
    final String errorMessage;

    public ExceptionNotMatched(ErrorCode code) {
        this.errorCode = code.getCode();
        this.errorMessage = code.getMessage();
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
