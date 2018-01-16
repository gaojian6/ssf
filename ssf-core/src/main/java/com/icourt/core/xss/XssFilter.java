/*
 * @date 2016年11月21日 10:46
 */
package com.icourt.core.xss;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author june
 */
public class XssFilter extends OncePerRequestFilter {

    private final XssCommonsMultipartResolver resolver;
    private final static String encoding = "utf-8";

    public XssFilter(XssCommonsMultipartResolver resolver){
        this.resolver = resolver;
    }

    @Override
    protected void initFilterBean() throws ServletException {
        resolver.setDefaultEncoding(encoding);
//        resolver.setMaxUploadSize(4096000);
        resolver.setServletContext(getServletContext());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        boolean multipartRequestParsed = false;
        HttpServletRequest req = new XssHttpServletRequestWrapper(request);
        if(resolver.isMultipart(req)){
//            req = resolver.resolveMultipart(req);
            multipartRequestParsed = true;
        }
        try{
            filterChain.doFilter(req, response);
        }finally{
            if(multipartRequestParsed){
//                resolver.cleanupMultipart(WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class));
            }
        }
    }

}
