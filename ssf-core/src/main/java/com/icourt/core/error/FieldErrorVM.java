/*
 * @date 2016年11月24日 22:28
 */
package com.icourt.core.error;

/**
 * field errors
 * @author june
 */
public class FieldErrorVM {

    private final String objectName;

    private final String field;

    private final String errorMsg;

    public FieldErrorVM(String dto, String field, String errorMsg) {
        this.objectName = dto;
        this.field = field;
        this.errorMsg = errorMsg;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
