/*
 * @date 2016年11月21日 10:39
 */
package com.icourt.core.xss;

import com.icourt.common.XSSKit;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * todo 具有xss功能的multipart解析器
 * 解决 enctype="multipart/form-data" 的方式提交数据的xss漏洞
 * @author june
 */
public class XssCommonsMultipartResolver extends CommonsMultipartResolver {

    public XssCommonsMultipartResolver() {

    }

    @Override
    public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
        MultipartParsingResult parsingResult = parseRequest(request);
        return new DefaultMultipartHttpServletRequest(request, parsingResult.getMultipartFiles(),
                parsingResult.getMultipartParameters(), parsingResult.getMultipartParameterContentTypes()) {

            @Override
            public String getParameter(String name) {
                String value = super.getParameter(name);
                return value == null ? null : XSSKit.htmlEscape(value);
            }

            @Override
            public String[] getParameterValues(String name) {
                String[] values = super.getParameterValues(name);
                if (values == null) {
                    return null;
                }
                String[] result = new String[values.length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = XSSKit.htmlEscape(values[i]);
                }
                return result;
            }

            @Override
            public String getHeader(String name) {
                String value = super.getHeader(name);
                return value == null ? null : XSSKit.htmlEscape(value);
            }

        };
    }

}
