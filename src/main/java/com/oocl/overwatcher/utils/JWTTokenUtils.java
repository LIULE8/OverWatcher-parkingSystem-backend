package com.oocl.overwatcher.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JWTTokenUtils {

    private final Logger log = LoggerFactory.getLogger(JWTTokenUtils.class);

    private static final String AUTHORITIES_KEY = "roles";
    //签名密钥
    private String secretKey;
    //失效日期
    private long tokenValidityInMilliseconds;
    //（记住我）失效日期
    private long tokenValidityInMillisecondsForRememberMe;

    @PostConstruct
    public void init() {
        this.secretKey = "overwatcher";
        int secondIn1day = 1000 * 60 * 60 * 24;
        this.tokenValidityInMilliseconds = secondIn1day * 2L;
        this.tokenValidityInMillisecondsForRememberMe = secondIn1day * 7L;
    }

    private final static long EXPIRATIONTIME = 432_000_000;

    //创建Token
    public String createToken(Authentication authentication, Boolean rememberMe) {
        //获取用户的角色字符串，如 USER,ADMIN
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        //获取当前时间戳
        long now = (new Date()).getTime();
        //存放过期时间
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        } else {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        }
        //创建Token令牌
        return Jwts.builder()
                //设置面向用户
                .setSubject(authentication.getName())
                //添加角色属性
                .claim(AUTHORITIES_KEY, authorities)
                //设置失效时间
                .setExpiration(validity)
                //生成签名
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    //获取用户权限
    public Authentication getAuthentication(String token) {
        System.out.println("token:" + token);
        //解析Token的payload
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                //获取用户权限字符串
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        //将元素转换为GrantedAuthority接口集合
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    //验证Token是否正确
    public boolean validateToken(String token) {
        try {
            //通过密钥验证Token
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {                                     //签名异常
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {                                 //JWT格式错误
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {                                   //JWT过期
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {                               //不支持该JWT
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {                              //参数错误异常
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
