package com.icourt.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedList;
import java.util.List;


@ConfigurationProperties(prefix = "ssf.security", ignoreUnknownFields = false)
public class JwtProperties {

    private Jwt jwt = new Jwt();

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public static class Jwt {

        //签名密钥
        private String secret = "my-secret-token-to-change-in-production";

        //token的key
        private String authorizationHeader = "token";

        /**
         * 不需要授权的url
         * 如 get /user/api/v1/login
         * 请求方法和请求url 空格隔开 如果不限制请求方法为空即可如/user/api/v1/login
         */
        private List<String> unAuthUrls = new LinkedList<>();

        public List<String> getUnAuthUrls() {
            return unAuthUrls;
        }

        public void setUnAuthUrls(List<String> unAuthUrls) {
            this.unAuthUrls = unAuthUrls;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getAuthorizationHeader() {
            return authorizationHeader;
        }

        public void setAuthorizationHeader(String authorizationHeader) {
            this.authorizationHeader = authorizationHeader;
        }
    }

}
