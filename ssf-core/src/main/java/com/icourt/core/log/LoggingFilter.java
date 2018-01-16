/*
 * @date 2017年08月11日 下午5:51
 */
package com.icourt.core.log;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author june
 */
public class LoggingFilter extends OncePerRequestFilter {

    private ILogger logger;

    public LoggingFilter(ILogger logger){
        this.logger = logger;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        try{
            logger.start(request);
            filterChain.doFilter(request, response);
        }finally{
            LoggerContext.remove();
            ThreadLocalDateUtil.remove();
        }
    }
}
