package com.icourt.security.jwt;

import com.icourt.common.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
public class JwtFilter extends GenericFilterBean {

    protected TokenProvider tokenProvider;
    protected JwtProperties jwtProperties;

    private PathMatcher pathMatcher = new AntPathMatcher();

    public JwtFilter(TokenProvider tokenProvider,JwtProperties jwtProperties) {
        this.tokenProvider = tokenProvider;
        this.jwtProperties = jwtProperties;
    }

    private Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            //需要拦截的url
            if(!ignoreURI(request)){
                //校验token
                String token = resolveToken(request);
                if(StrKit.isEmpty(token)){
                    logger.error("token is null,token:{},uri:{}",token,request.getRequestURI());
                }
                String errorMsg = tokenProvider.validateTokenReturnInfo(token);
                if(errorMsg == null){
                    doTokenValid(request,response,filterChain,token);
                }else{
                    doTokenInValid(request,response,filterChain,token,errorMsg);
                }
                return;
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            SecurityContext.remove();
        }
    }

    /**
     * token有效的时处理
     * @param request request
     * @param response response
     * @param filterChain filterChain
     * @param token token
     */
    protected void doTokenValid(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain,String token) throws IOException, ServletException{
        Object data = tokenProvider.parseToken(token,Object.class);
        SecurityContext.setTokenData(data);
        SecurityContext.setToken(token);
        filterChain.doFilter(request, response);
    }

    /**
     * token无效处理方式
     * @param request  request
     * @param response response
     * @param filterChain filterChain
     * @param token token
     * @param errorMsg 出错信息
     */
    protected void doTokenInValid(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain,String token,String errorMsg) throws IOException,
            ServletException{
        response.addHeader("errorMsg",errorMsg);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    /**
     * 忽略某些请求的授权校验
     * @param request request
     * @return true忽略检查 false需要检查
     */
    protected boolean ignoreURI(HttpServletRequest request) {

        String requestURI = request.getRequestURI();

        //不用验证的url
        List<String> unAuthUrls = jwtProperties.getJwt().getUnAuthUrls();

        //遍历不用验证的url 是否和当前url匹配
        for (String unAuthUrl : unAuthUrls) {
            if (StrKit.isEmpty(unAuthUrl)) {
                continue;
            }
            String currMethod = null;
            if (unAuthUrl.contains(" ")) {
                String[] array = unAuthUrl.split(" ");
                currMethod = array[0];
                unAuthUrl = array[1];
            }
            //请求方法和url都匹配，如果匹配 不用验证token
            if ((currMethod == null || currMethod.equalsIgnoreCase(request.getMethod()))
                    && pathMatcher.match(unAuthUrl, requestURI)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取token
     * @param request request
     * @return token
     */
    protected String resolveToken(HttpServletRequest request){
        String headKey = jwtProperties.getJwt().getAuthorizationHeader();
        String method = request.getMethod();
        //先从head取
        String token = request.getHeader(headKey);
        //如果token为null，并且是get请求，则从参数再获取一次
        if(StrKit.isEmpty(token) && method.equalsIgnoreCase("GET")){
            token = request.getParameter(headKey);
        }
        return token;
    }
}
