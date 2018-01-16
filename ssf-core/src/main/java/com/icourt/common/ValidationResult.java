/**
 * @date 2016年09月08日 17:19
 */
package com.icourt.common;

import java.util.Map;

/**
 * 校验结果
 * @author june
 */
public class ValidationResult {

    //校验结果是否有错
    private boolean hasErrors;
    //第一条错误信息
    private String firstErrorMsg;

    //校验错误信息
    private Map<String,String> errorMsg;

    public boolean hasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public String getFirstErrorMsg() {
        return firstErrorMsg;
    }

    public void setFirstErrorMsg(String firstErrorMsg) {
        this.firstErrorMsg = firstErrorMsg;
    }

    public Map<String, String> getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Map<String, String> errorMsg) {
        this.errorMsg = errorMsg;
    }
}
