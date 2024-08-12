package com.ihorpolataiko.springbootsecurityweb.security.apikey;

import com.ihorpolataiko.springbootsecurityweb.common.AuthConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthenticationConverter implements AuthenticationConverter {

  @Override
  public Authentication convert(HttpServletRequest request) {

    String apiKey = request.getHeader(AuthConstants.API_KEY_AUTHORIZATION_HEADER);

    if (apiKey == null || apiKey.isEmpty()) {
      return null;
    }

    return ApiKeyAuthentication.unauthenticated(apiKey);
  }
}
