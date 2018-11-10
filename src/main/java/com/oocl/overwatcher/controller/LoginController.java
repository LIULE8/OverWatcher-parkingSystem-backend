package com.oocl.overwatcher.controller;

import com.oocl.overwatcher.config.WebSecurityConfig;
import com.oocl.overwatcher.dto.LoginDTO;
import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.repositories.UserRepository;
import com.oocl.overwatcher.utils.JWTTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by linyuan on 2017/12/13.
 */
@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenUtils jwtTokenUtils;

    @PostMapping("/auth/login")
    public Map<String, String> login(@RequestBody LoginDTO loginDTO, HttpServletResponse httpResponse) throws Exception {
        //通过用户名和密码创建一个 Authentication 认证对象，实现类为 UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        //如果认证对象不为空
        userRepository.findByUserName(authenticationToken.getPrincipal().toString())
                .orElseThrow(() -> new Exception("用户不存在"));
        try {
            Map<String, String> map = new HashMap<>();
            User user = userRepository.findByUserNameAndAlive(authenticationToken.getPrincipal().toString(), true).orElse(null);
            if (user != null) {
                //通过 AuthenticationManager（默认实现为ProviderManager）的authenticate方法验证 Authentication 对象
                Authentication authentication = authenticationManager.authenticate(authenticationToken);
                //将 Authentication 绑定到 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //生成Token
                String token = jwtTokenUtils.createToken(authentication, false);
                //将Token写入到Http头部
                httpResponse.addHeader(WebSecurityConfig.AUTHORIZATION_HEADER, token);
                map.put("roles", user.getRoleList().get(0).getName());
                map.put("token", token);
                map.put("id", user.getId() + "");
                map.put("username", user.getUserName());
                map.put("msg", "true");
            } else {
                map.put("msg", "该用户已经被冻结");
            }
            return map;
        } catch (BadCredentialsException authentication) {
            throw new Exception("密码错误");
        }
    }

    @PutMapping("/userLogout")
    public ResponseEntity logout(Long id) {
        return ResponseEntity.ok().build();
    }
}
