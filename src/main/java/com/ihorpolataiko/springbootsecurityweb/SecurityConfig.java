package com.ihorpolataiko.springbootsecurityweb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

    http.authenticationManager(authenticationManager)
        .authorizeHttpRequests(matcher -> matcher.anyRequest().permitAll());

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return authentication -> authentication;
  }
}
