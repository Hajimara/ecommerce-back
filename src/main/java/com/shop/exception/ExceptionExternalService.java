package com.shop.exception;

import com.shop.constant.enums.ErrorCode;
import com.shop.constant.interfaces.ExceptionResult;

public class ExceptionExternalService extends RuntimeException implements ExceptionResult {

    final int errorCode;
    final String errorMessage;

    public ExceptionExternalService(ErrorCode code) {
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
