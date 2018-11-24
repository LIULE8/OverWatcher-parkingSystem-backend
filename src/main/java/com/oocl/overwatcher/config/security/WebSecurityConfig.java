package com.oocl.overwatcher.config.security;

import com.oocl.overwatcher.filter.JwtAuthenticationTokenFilter;
import com.oocl.overwatcher.filter.MyCorsFilter;
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

    private static final String LOGIN_PAGE = "http://cell.nat300.top/";

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final MyCorsFilter myCorsFilter;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, MyCorsFilter myCorsFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.myCorsFilter = myCorsFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
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
//        .antMatchers("/parkingLots/**").permitAll()
//        .antMatchers("/parkingBoys/**").permitAll()
//        .antMatchers("/orders/**").permitAll()
//        .antMatchers("/employees/**").permitAll()
//        .antMatchers("/parkingBoy/**").permitAll()
//        .antMatchers("/userLogout/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage(LOGIN_PAGE).permitAll()
            .and()
            //设置登出
            .logout().logoutSuccessUrl(LOGIN_PAGE).permitAll()
            .and()
            .addFilterBefore(myCorsFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 自定义密码的编码方式
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
