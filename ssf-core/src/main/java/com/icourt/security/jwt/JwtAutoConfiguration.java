package com.icourt.security.jwt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass({io.jsonwebtoken.Jwts.class})
public class JwtAutoConfiguration {

    @Bean
    public TokenProvider tokenProvider(){
        return new TokenProvider();
    }

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

}
