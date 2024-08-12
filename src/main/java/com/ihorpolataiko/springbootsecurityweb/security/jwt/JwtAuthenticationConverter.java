package com.ihorpolataiko.springbootsecurityweb.security.jwt;

import com.ihorpolataiko.springbootsecurityweb.common.AuthConstants;
import com.ihorpolataiko.springbootsecurityweb.security.jwt.exception.TokenAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {

  @Override
  public Authentication convert(HttpServletRequest request) {

    String authenticationHeader = request.getHeader(AuthConstants.JWT_AUTHORIZATION_HEADER);

    if (authenticationHeader == null || authenticationHeader.isEmpty()) {
      return null;
    }

    String jwtToken = stripBearerPrefix(authenticationHeader);

    return JwtAuthentication.unauthenticated(jwtToken);
  }

  String stripBearerPrefix(String token) {

    if (!token.startsWith("Bearer")) {
      throw new TokenAuthenticationException("Unsupported authentication scheme");
    }

    return token.substring(7);
  }
}
