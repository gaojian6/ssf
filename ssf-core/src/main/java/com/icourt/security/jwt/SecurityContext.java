package com.icourt.security.jwt;


public class SecurityContext {

    private final static ThreadLocal<Object> tokenDataThreadLocal = new ThreadLocal<>();
    private final static ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();


    public static void setTokenData(Object obj) {
        tokenDataThreadLocal.set(obj);
    }

    public static Object getTokenData() {
        return tokenDataThreadLocal.get();
    }

    public static void remove(){
        tokenDataThreadLocal.remove();
        tokenThreadLocal.remove();
    }

    public static void setToken(String token) {
        tokenThreadLocal.set(token);
    }

    public static String getToken() {
        return tokenThreadLocal.get();
    }


}
