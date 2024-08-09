package com.ihorpolataiko.springbootsecurityweb.security.configurer;

import com.ihorpolataiko.springbootsecurityweb.security.converter.JwtAuthenticationConverter;
import com.ihorpolataiko.springbootsecurityweb.security.filter.JwtAuthenticationFilter;
import com.ihorpolataiko.springbootsecurityweb.security.provider.JwtAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationConfigurer
    extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  private final JwtAuthenticationConverter jwtAuthenticationConverter;

  private final AuthenticationEntryPoint authenticationEntryPoint;

  public JwtAuthenticationConfigurer(
      JwtAuthenticationProvider jwtAuthenticationProvider,
      JwtAuthenticationConverter jwtAuthenticationConverter,
      AuthenticationEntryPoint authenticationEntryPoint) {
    this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Override
  public void init(HttpSecurity http) {
    http.authenticationProvider(jwtAuthenticationProvider);
  }

  @Override
  public void configure(HttpSecurity http) {

    AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

    http.addFilterBefore(
        new JwtAuthenticationFilter(
            authenticationManager, jwtAuthenticationConverter, authenticationEntryPoint),
        AuthorizationFilter.class);
  }
}
