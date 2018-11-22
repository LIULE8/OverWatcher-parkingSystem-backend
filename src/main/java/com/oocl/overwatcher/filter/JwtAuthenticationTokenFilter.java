package com.oocl.overwatcher.filter;

import com.oocl.overwatcher.config.security.WebSecurityConfig;
import com.oocl.overwatcher.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIULE9
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends GenericFilterBean {


  private final JwtUtils jwtUtils;

  @Autowired
  public JwtAuthenticationTokenFilter(JwtUtils jwtUtils) {
    this.jwtUtils = jwtUtils;
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    try {
      log.info("jwt filter");
      HttpServletRequest request = (HttpServletRequest) servletRequest;
      String jwt = getTokenFromRequestHeader(request);
      if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
        //获取用户认证信息
        Authentication authentication = jwtUtils.getAuthenticationParsingJwt(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(servletRequest, servletResponse);
      } else {
        ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
    } catch (ExpiredJwtException e) {
      log.warn("Security exception user={}, reason={}", e.getClaims().getSubject(), e.getMessage());
      ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  /**
   * 从请求头部获取token，如果token放在cookie，也可改成从cookie中获取
   *
   * @param request
   * @return
   */
  private String getTokenFromRequestHeader(HttpServletRequest request) {
    //从HTTP头部获取TOKEN
    String token = request.getHeader(WebSecurityConfig.AUTHORIZATION_HEADER);
    if (StringUtils.hasText(token)) {
      return token;
    }
    return null;
  }
}
