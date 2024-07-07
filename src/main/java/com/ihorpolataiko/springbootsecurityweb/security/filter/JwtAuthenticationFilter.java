package com.ihorpolataiko.springbootsecurityweb.security.filter;

import com.ihorpolataiko.springbootsecurityweb.security.converter.JwtAuthenticationConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
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

    // Beware, that AuthenticationEntryPoint registered via DSL
    // will NOT be invoked while handling authentication exceptions
    // thrown by the authentication manager inside of this filter,
    // because the filter catch the exception and does not rethrow it,
    // so we need to implement a failure handler
    setFailureHandler(authenticationEntryPoint::commence);
  }
}
