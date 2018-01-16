/**
 * @date 2016年09月08日 17:13
 */
package com.icourt.common;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具
 *
 * @author june
 */
public class ValidatorKit {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 验证某个实体类
     *
     * @param obj 实例
     * @param <T> 类型
     * @return 校验结果
     */
    public static <T> ValidationResult validateEntity(T obj) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (set!=null && !set.isEmpty()) {
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<>();
            String firstErrorMsg = null;
            for (ConstraintViolation<T> cv : set) {
                if (firstErrorMsg == null) {
                    firstErrorMsg = cv.getMessage();
                }
                errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
            }
            result.setFirstErrorMsg(firstErrorMsg);
            result.setErrorMsg(errorMsg);
        }
        return result;
    }

    /**
     * 验证某个实体的某个属性
     *
     * @param obj          实例
     * @param propertyName 属性名
     * @param <T>          类型
     * @return 结果
     */
    public static <T> ValidationResult validateProperty(T obj, String propertyName) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, Default.class);
        if (set!=null && !set.isEmpty()) {
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<>();
            String firstErrorMsg = null;
            for (ConstraintViolation<T> cv : set) {
                if (firstErrorMsg == null) {
                    firstErrorMsg = cv.getMessage();
                }
                errorMsg.put(propertyName, cv.getMessage());
            }
            result.setFirstErrorMsg(firstErrorMsg);
            result.setErrorMsg(errorMsg);
        }
        return result;
    }

    /**
     * 校验是否是手机号
     * @param mobile 手机号
     * @return true or false
     */
    public static boolean isMobileNo(String mobile) {
        return matcher(mobile,RegexConstants.MOBILE);
    }

    /**
     * 是否匹配
     * @param input 输入
     * @param regex 正则
     * @return true or false
     */
    public static boolean matcher(String input,String regex){
        if(input==null){
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        return m.matches();
    }

}
