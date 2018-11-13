package com.oocl.overwatcher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author LIULE9
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/*")
        .allowedOrigins("*")
        .allowedMethods("POST", "GET", "OPTIONS", "DELETE", "PUT")
        .allowedHeaders("x-requested-with", "Authorization", "Content-Type")
        .exposedHeaders("Authorization", "x-requested-with")
        .allowCredentials(false).maxAge(3600);
  }
}
