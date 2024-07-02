package com.ihorpolataiko.springbootsecurityweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

  // password encoder is required by the UserService, which would create circular dependency if we
  // put this bean declaration to the SecurityConfig class (because, UserService is used by
  // CustomAuthenticationManager, which in turn is required by SecurityConfig). There are
  // many possible ways to solve it, but let's just put this bean declaration into another class
  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
}
