package com.icourt.security.jwt;

import com.icourt.common.SafeKit;
import com.icourt.common.StrKit;
import com.icourt.core.json.IJsonService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Date;

public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String PAYLOAD_KEY = "payload";

    private String secretKey;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private IJsonService jsonService;


    @PostConstruct
    public void init() {
        this.secretKey = jwtProperties.getJwt().getSecret();
    }

    /**
     * 生成token
     * @param t token信息
     * @param expTime 过期时间 毫秒
     * @return token
     */
    public <T> String createToken(T t, long expTime) {
        Assert.notNull(t,"token内容不能为null");
        return Jwts.builder()
            .claim(PAYLOAD_KEY, jsonService.toJSONString(t)) //其他信息
            .signWith(SignatureAlgorithm.HS512, secretKey) //签名加密方式
            .setExpiration(new Date(System.currentTimeMillis()+expTime)) //token过期时间
            .compact();
    }

    /**
     * 解析token中的数据
     * @param token token
     * @param clazz 数据类型
     * @param <T> 类型
     * @return token中的数据
     */
    public  <T> T parseToken(String token,Class<T> clazz) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
        String payload = SafeKit.getString(claims.get(PAYLOAD_KEY));
        System.out.println("payload"+payload);
        if(StrKit.isNotEmpty(payload)){
            return jsonService.parseObject(payload,clazz);
        }
       return null;
    }

    /**
     * 校验token
     * @param token token
     * @return true 有效token false 无效token
     */
    public boolean validateToken(String token) {
        if(StrKit.isEmpty(token)){
            log.info("token is null. token：{}",token);
            return false;
        }
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature. token：{}",token);
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token. token：{}",token);
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token. token：{}",token);
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token. token：{}",token);
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid. token：{}",token);
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

    /**
     * 校验token,并返回出错信息
     * @param token token
     * @return 有效token 返回null 无效token 返回错误信息
     */
    public String validateTokenReturnInfo(String token) {
        if(StrKit.isEmpty(token)){
            log.info("token is null. token：{}",token);
            return "token is null";
        }
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return null;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature. token：{}",token);
            log.trace("Invalid JWT signature trace: {}", e);
            return "Invalid JWT signature.";
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token. token：{}",token);
            log.trace("Invalid JWT token trace: {}", e);
            return "Invalid JWT token.";
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token. token：{}",token);
            log.trace("Expired JWT token trace: {}", e);
            return "Expired JWT token.";
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token. token：{}",token);
            log.trace("Unsupported JWT token trace: {}", e);
            return "Unsupported JWT token.";
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid. token：{}",token);
            log.trace("JWT token compact of handler are invalid trace: {}", e);
            return "JWT token compact of handler are invalid.";
        }
    }

}
