package com.oocl.overwatcher.config.security;

import com.oocl.overwatcher.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author LIULE9
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  public static final String AUTHORIZATION_HEADER = "Authorization";


  public static final String LOGIN_PAGE = "/";

  public static final String AUTHORIZATION_TOKEN = "access_token";

  private final UserDetailsServiceImpl userDetailsService;

  @Autowired
  public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        //自定义获取用户信息
        .userDetailsService(userDetailsService)
        //设置密码加密
        .passwordEncoder(passwordEncoder());
  }

  /**
   * 配置请求访问策略
   *
   * @param http : 提供授权的配置，可以自定义请求访问策略
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        //由于使用Token，所以不需要Session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/", "/auth/login").permitAll()
        .antMatchers("/parkingLots/**").permitAll()
        .antMatchers("/parkingBoys/**").permitAll()
        .antMatchers("/orders/**").permitAll()
        .antMatchers("/employees/**").permitAll()
        .antMatchers("/parkingBoy/**").permitAll()
        .antMatchers("/userLogout/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage(LOGIN_PAGE).permitAll()
        .and()
        //设置登出
        .logout().logoutSuccessUrl(LOGIN_PAGE).permitAll()
        .and()
        //添加JWT filter
        .addFilterBefore(genericFilterBean(), UsernamePasswordAuthenticationFilter.class);
  }

  /**
   * 自定义密码的编码方式
   * @return
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public GenericFilterBean genericFilterBean() {
    return new JwtAuthenticationTokenFilter();
  }

  @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
