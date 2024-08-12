package com.ihorpolataiko.springbootsecurityweb.security.jwt;

import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final JwtService jwtService;

  public JwtAuthenticationProvider(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;

    AuthUser authUser = jwtService.resolveJwtToken(jwtAuthentication.getCredentials());

    return JwtAuthentication.authenticated(authUser);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthentication.class.isAssignableFrom(authentication);
  }
}
