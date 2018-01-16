/*
 * @date 2016年12月26日 20:09
 */
package com.icourt.orm.filter;

import com.icourt.common.StrKit;
import com.icourt.common.XSSKit;
import com.icourt.core.error.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 支持绑定Criteria参数
 * @author june
 */
public class CriteriaArgumentResolver implements HandlerMethodArgumentResolver {

    private final CriteriaParser criteriaParser;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public CriteriaArgumentResolver(CriteriaParser criteriaParser) {
        this.criteriaParser = criteriaParser;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Criteria.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String criteriaStr = request.getParameter("criteria");
        if(StrKit.isNotEmpty(criteriaStr)) {
            try {
                String criteriaJson = XSSKit.jsonHtmlUnescape(criteriaStr);
                criteriaJson = criteriaJson.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
                return criteriaParser.parse(criteriaJson);
            } catch (Exception e) {
                logger.error("criteria:"+criteriaStr,e);
                throw new ParamException("criteria参数有误");
            }
        }
        return null;
    }

}
