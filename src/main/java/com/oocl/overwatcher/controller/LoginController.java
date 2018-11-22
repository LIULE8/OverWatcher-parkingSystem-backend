package com.oocl.overwatcher.controller;

import com.oocl.overwatcher.config.security.WebSecurityConfig;
import com.oocl.overwatcher.dto.LoginDTO;
import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.enums.LoginStatusEnum;
import com.oocl.overwatcher.exceptions.ParkingSystemException;
import com.oocl.overwatcher.forms.LoginForm;
import com.oocl.overwatcher.repositories.UserRepository;
import com.oocl.overwatcher.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @author LIULE9
 */
@RestController
@CrossOrigin
@Slf4j
public class LoginController {

  private final UserRepository userRepository;

  private final AuthenticationManager authenticationManager;

  private final JwtUtils jwtUtils;

  @Autowired
  public LoginController(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/auth/login")
  public ResponseEntity<LoginDTO> login(@RequestBody LoginForm loginForm) {
    try {
      User user = userRepository.findUserByUserNameAndAliveEquals(loginForm.getUsername(), true)
          .orElseThrow(() -> new ParkingSystemException(LoginStatusEnum.FAIL_USERNAME.getCode(), LoginStatusEnum.FAIL_USERNAME.getMessage()));

      //spring security
      Authentication request = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
      Authentication result = authenticationManager.authenticate(request);
      SecurityContextHolder.getContext().setAuthentication(result);

      //Token
      String token = jwtUtils.createToken(result, false);

      LoginDTO loginDTO = new LoginDTO(user.getRoleList().get(0).getName(), token, String.valueOf(user.getId()), user.getUserName(), "true");

      return ResponseEntity.ok().header(WebSecurityConfig.AUTHORIZATION_HEADER, token).body(loginDTO);

    } catch (ParkingSystemException e) {
      log.error("【认证登录】 ".concat(e.getMessage())
          .concat(", username={}"), loginForm.getUsername());
      return ResponseEntity.badRequest().body(new LoginDTO("该用户已经被冻结"));
    } catch (BadCredentialsException e) {
      log.error("【认证登录】 密码错误".concat(", username={}, password"), loginForm.getUsername(), loginForm.getPassword());
      throw new ParkingSystemException(LoginStatusEnum.FAIL_PASSWORD.getCode(), LoginStatusEnum.FAIL_PASSWORD.getMessage());
    }
  }

  @PutMapping("/userLogout")
  public ResponseEntity logout(Long id) {
    return ResponseEntity.ok().build();
  }
}
