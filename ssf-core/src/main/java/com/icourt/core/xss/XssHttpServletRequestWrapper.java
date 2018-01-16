/*
 * @date 2016年11月21日 10:42
 */
package com.icourt.core.xss;


import com.icourt.common.XSSKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * xss过滤包装器
 *
 * @author june
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return value == null ? null : XSSKit.htmlEscape(value);
    }

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
        if (values.length == 0) {
            return new String[]{};
        }
        String[] result = new String[values.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = XSSKit.htmlEscape(values[i]);
        }
        return result;
    }

}
