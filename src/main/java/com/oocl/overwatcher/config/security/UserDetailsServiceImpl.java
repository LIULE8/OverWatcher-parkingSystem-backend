package com.oocl.overwatcher.config.security;


import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author LIULE9
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findByUserName(username);
    userOptional.ifPresent((user) -> System.out.println("用户名:" + user.getUserName() + " 用户密码：" + user.getPassword()));
    return new UserDetailsImpl(userOptional.orElse(new User(username, "")));
  }
}
