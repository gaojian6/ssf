/*
 * @date 2016年11月24日 22:50
 */
package com.icourt.core.error;

/**
 * 参数错误异常
 * @author june
 */
public class ParamException extends RuntimeException{

    private final String errorCode;
    private final String errorMsg;

    public ParamException(String errorCode,String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public ParamException(String errorMsg) {
        this(ErrorConstants.ERR_VALIDATION,errorMsg);
    }

    public ErrorVM getErrorVM() {
        return new ErrorVM(errorCode, errorMsg);
    }

}
