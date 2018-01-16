/*
 * @date 2016年12月11日 19:43
 */
package com.icourt.data;

import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于请求结束时缓存回收ThreadLocal
 * @author june
 */
public class CacheFilter extends OncePerRequestFilter {

    private final ApplicationContext applicationContext;

    public CacheFilter(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }finally{
            String[] allCacheBeanNames = applicationContext.getBeanNamesForType(ICache.class);
            if(allCacheBeanNames!=null && allCacheBeanNames.length>0){
                for (String cacheBeanName : allCacheBeanNames) {
                    ICache cache = applicationContext.getBean(cacheBeanName,ICache.class);
                    if(cache!=null){
                        cache.destroy();
                    }
                }
            }
        }
    }
}
