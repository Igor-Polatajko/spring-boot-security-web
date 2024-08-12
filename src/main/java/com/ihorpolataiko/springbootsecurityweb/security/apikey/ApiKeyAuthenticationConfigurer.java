package com.ihorpolataiko.springbootsecurityweb.security.apikey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthenticationConfigurer
    extends AbstractHttpConfigurer<ApiKeyAuthenticationConfigurer, HttpSecurity> {

  private final ApiKeyAuthenticationProvider apiKeyAuthenticationProvider;

  private final ApiKeyAuthenticationConverter apiKeyAuthenticationConverter;

  private final AuthenticationEntryPoint authenticationEntryPoint;

  public ApiKeyAuthenticationConfigurer(
      ApiKeyAuthenticationProvider apiKeyAuthenticationProvider,
      ApiKeyAuthenticationConverter apiKeyAuthenticationConverter,
      AuthenticationEntryPoint authenticationEntryPoint) {
    this.apiKeyAuthenticationProvider = apiKeyAuthenticationProvider;
    this.apiKeyAuthenticationConverter = apiKeyAuthenticationConverter;
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Override
  public void init(HttpSecurity http) {
    http.authenticationProvider(apiKeyAuthenticationProvider);
  }

  @Override
  public void configure(HttpSecurity http) {

    AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

    http.addFilterBefore(
        new ApiKeyAuthenticationFilter(
            authenticationManager, apiKeyAuthenticationConverter, authenticationEntryPoint),
        AuthorizationFilter.class);
  }
}
