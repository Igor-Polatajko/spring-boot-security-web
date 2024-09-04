package com.ihorpolataiko.springbootsecurityweb.security.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFilter;

public class JwtAuthenticationFilter extends AuthenticationFilter {

  public JwtAuthenticationFilter(
      AuthenticationManager authenticationManager,
      JwtAuthenticationConverter jwtAuthenticationConverter,
      AuthenticationEntryPoint authenticationEntryPoint) {

    super(authenticationManager, jwtAuthenticationConverter);

    // Beware, that the default success handler in AuthenticationFilter will do a redirect to '/'
    // If you encounter a redirect loop problem during a similar implementation
    // the solution will be to override the default success handler
    setSuccessHandler((request, response, authentication) -> {});

    // Beware, that AuthenticationEntryPoint, registered
    // via HttpSecurity DSL (and used by ExceptionTranslationFilter),
    // will NOT be invoked while handling authentication exceptions
    // thrown by the authentication manager inside of this filter
    // because the AuthenticationFilter catches the exception
    // and does not rethrow it, so we need to implement a failure handler,
    // which, by the way, might simply rethrow an exception
    // to let it be handled by ExceptionTranslationFilter
    setFailureHandler(authenticationEntryPoint::commence);
  }
}
