package com.oocl.overwatcher.config;

import com.oocl.overwatcher.filter.JwtAuthenticationTokenFilter;
import com.oocl.overwatcher.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    public static final String AUTHORIZATION_TOKEN = "access_token";

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //自定义获取用户信息
                .userDetailsService(userDetailsService)
                //设置密码加密
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置请求访问策略
        http
                //关闭CSRF、CORS
                .cors().and()
                .csrf().disable()
                //由于使用Token，所以不需要Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //验证Http请求
                .authorizeRequests()
                //允许所有用户访问首页 与 登录
                .antMatchers("/", "/auth/login").permitAll()
                .antMatchers("/parkingLots/**").permitAll()
                .antMatchers("/parkingBoys/**").permitAll()
                .antMatchers("/orders/**").permitAll()
                .antMatchers("/employees/**").permitAll()
                .antMatchers("/parkingBoy/**").permitAll()
                .antMatchers("/userLogout/**").permitAll()
                //其它任何请求都要经过认证通过
                .anyRequest().authenticated()
                //用户页面需要用户权限
//                .antMatchers("/userpage").hasAnyRole("USER")
                .and()
                //设置登出
                .logout().permitAll();
        //添加JWT filter 在
        http.addFilterBefore(genericFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

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
