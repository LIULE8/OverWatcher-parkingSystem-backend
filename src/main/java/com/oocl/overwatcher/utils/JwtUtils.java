package com.oocl.overwatcher.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author LIULE9
 */
@Slf4j
@Component
public class JwtUtils {

  private static final String AUTHORITIES_KEY = "roles";

  /**
   * 一天的秒数
   */
  private static final long SECOND_IN_ONE_DAY = 1000 * 60 * 60 * 24;

  /**
   * 签名密钥
   */
  private static final String SECRET_KEY = "overWatcher";

  /**
   * TOKEN 一般失过期日期
   */
  private static final long TOKEN_VALIDITY_IN_MILLISECONDS = SECOND_IN_ONE_DAY * 2L;

  /**
   * TOKEN（记住我）过期日期
   */
  private static final long TOKEN_VALIDITY_IN_MILLISECONDS_FOR_REMEMBER_ME = SECOND_IN_ONE_DAY * 7L;


  /**
   * 创建Token
   *
   * @param authentication
   * @param rememberMe
   * @return
   */
  public String createToken(Authentication authentication, Boolean rememberMe) {
    // 获取用户的角色字符串，如 USER,ADMIN
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));
    // 获取当前时间戳
    long now = (new Date()).getTime();
    // token 过期时间
    Date validity;
    if (rememberMe) {
      validity = new Date(now + TOKEN_VALIDITY_IN_MILLISECONDS_FOR_REMEMBER_ME);
    } else {
      validity = new Date(now + TOKEN_VALIDITY_IN_MILLISECONDS);
    }

    // 使用 jwt 包创建Token字符串
    return Jwts.builder()
        // 设置面向用户，用户名
        .setSubject(authentication.getName())
        // 添加角色属性，权限
        .claim(AUTHORITIES_KEY, authorities)
        // 设置过期时间
        .setExpiration(validity)
        // 生成签名 over watcher
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .compact();
  }

  /**
   * 根据 token 获取用户权限
   *
   * @param token
   * @return
   */
  public Authentication getAuthenticationParsingJwt(String token) {
    log.info("token={}" + token);
    //使用 jwt 包获取用户的权限的字符串
    Claims claims = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();

    //根据权限字符串，分割成一个list
    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
  }

  /**
   * 验证 Token 是否正确
   *
   * @param token
   * @return
   */
  public boolean validateToken(String token) {
    try {
      //通过密钥验证Token
      Jwts.parser()
          .setSigningKey(SECRET_KEY)
          .parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {                                     //签名异常
      log.warn("签名异常: {}", e.getMessage());
    } catch (MalformedJwtException e) {                                 //JWT格式错误
      log.warn("token 格式错误: {}", e.getMessage());
    } catch (ExpiredJwtException e) {                                   //JWT过期
      log.warn("token 过期: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.warn("token 非法: {}", e.getMessage());
    } catch (IllegalArgumentException e) {                              //参数错误异常
      log.warn("token 参数错误: {}", e.getMessage());
    }
    return false;
  }
}
