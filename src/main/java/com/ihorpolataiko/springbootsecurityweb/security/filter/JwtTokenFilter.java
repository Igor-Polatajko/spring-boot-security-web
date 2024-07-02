package com.ihorpolataiko.springbootsecurityweb.security.filter;

import com.ihorpolataiko.springbootsecurityweb.common.AuthConstants;
import com.ihorpolataiko.springbootsecurityweb.security.authentication.JwtAuthentication;
import com.ihorpolataiko.springbootsecurityweb.security.exception.TokenAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenFilter extends AbstractAuthenticationCreationFilter {

  @Override
  protected Authentication buildAuthentication(HttpServletRequest request) {

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
