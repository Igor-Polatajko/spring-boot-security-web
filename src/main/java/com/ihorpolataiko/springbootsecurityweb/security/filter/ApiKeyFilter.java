package com.ihorpolataiko.springbootsecurityweb.security.filter;

import com.ihorpolataiko.springbootsecurityweb.common.AuthConstants;
import com.ihorpolataiko.springbootsecurityweb.security.authentication.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyFilter extends AbstractAuthenticationCreationFilter {

  @Override
  protected Authentication buildAuthentication(HttpServletRequest request) {

    String apiKey = request.getHeader(AuthConstants.API_KEY_AUTHORIZATION_HEADER);

    if (apiKey == null || apiKey.isEmpty()) {
      return null;
    }

    return ApiKeyAuthentication.unauthenticated(apiKey);
  }
}
