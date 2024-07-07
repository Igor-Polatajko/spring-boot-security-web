package com.ihorpolataiko.springbootsecurityweb.security.provider;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import com.ihorpolataiko.springbootsecurityweb.config.properties.ApiKeyClientsProperties;
import com.ihorpolataiko.springbootsecurityweb.security.authentication.ApiKeyAuthentication;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

  private final Map<String, String> apiKeysToClientIds;

  public ApiKeyAuthenticationProvider(ApiKeyClientsProperties apiKeyClientsProperties) {
    this.apiKeysToClientIds =
        apiKeyClientsProperties.getClients().entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getValue, Map.Entry::getKey, (oldValue, newValue) -> oldValue));
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    ApiKeyAuthentication apiKeyAuthentication = (ApiKeyAuthentication) authentication;

    String apiKey = apiKeyAuthentication.getCredentials();

    if (!apiKeysToClientIds.containsKey(apiKey)) {
      throw new BadCredentialsException("API key is not valid");
    }

    String clientId = apiKeysToClientIds.get(apiKey);
    AuthUser authUser = new AuthUser(clientId, List.of(Role.ROLE_ADMIN));
    return ApiKeyAuthentication.authenticated(authUser);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return ApiKeyAuthentication.class.isAssignableFrom(authentication);
  }
}
