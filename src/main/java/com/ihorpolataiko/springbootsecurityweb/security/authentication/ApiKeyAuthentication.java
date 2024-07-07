package com.ihorpolataiko.springbootsecurityweb.security.authentication;

import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// Let's extend AbstractAuthenticationToken this time, so we will need to override fewer methods
public class ApiKeyAuthentication extends AbstractAuthenticationToken {

  // You might want to have different AuthenticationPrincipals for different authentications,
  // but let's stick to the AuthUser being a principal in both authentications in this example
  private final AuthUser authUser;

  private final String apiKey;

  private ApiKeyAuthentication(
      Collection<? extends GrantedAuthority> authorities,
      AuthUser authUser,
      boolean authenticated,
      String apiKey) {

    super(authorities);
    super.setAuthenticated(authenticated);

    this.authUser = authUser;
    this.apiKey = apiKey;
  }

  public static ApiKeyAuthentication unauthenticated(String apiKey) {

    return new ApiKeyAuthentication(null, null, false, apiKey);
  }

  public static ApiKeyAuthentication authenticated(AuthUser authUser) {

    Set<SimpleGrantedAuthority> authorities =
        authUser.roles().stream()
            .map(Enum::name)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

    return new ApiKeyAuthentication(authorities, authUser, true, null);
  }

  @Override
  public String getCredentials() {
    return apiKey;
  }

  @Override
  public Object getPrincipal() {
    return authUser;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    throw new UnsupportedOperationException();
  }
}
