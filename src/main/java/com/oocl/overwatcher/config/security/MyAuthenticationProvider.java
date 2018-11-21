package com.oocl.overwatcher.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author LIULE9 可以直接注入userRepository???
 */
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsServiceImpl userDetailsService;

  @Autowired
  public MyAuthenticationProvider(UserDetailsServiceImpl userDetailsService) {
    this.userDetailsService = userDetailsService;
  }


  @Override
  public Authentication authenticate(Authentication request) throws AuthenticationException {
    String username = (String) request.getPrincipal();
    String password = (String) request.getCredentials();
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    if (userDetails.getPassword().equals(password)) {
      return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }
    return null;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return aClass.equals(UsernamePasswordAuthenticationToken.class);
  }

}
