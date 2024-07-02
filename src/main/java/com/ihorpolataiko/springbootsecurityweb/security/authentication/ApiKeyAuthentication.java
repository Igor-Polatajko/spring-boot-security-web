package com.ihorpolataiko.springbootsecurityweb.security.authentication;

import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// You might want to have different AuthenticationPrincipals for different authentications,
// but let's stick to the AuthUser being a principal in both authentications in this example
public record ApiKeyAuthentication(AuthUser authUser, boolean authenticated, String apiKey)
    implements Authentication {

  public static ApiKeyAuthentication unauthenticated(String apiKey) {

    return new ApiKeyAuthentication(null, false, apiKey);
  }

  public static ApiKeyAuthentication authenticated(AuthUser authUser) {

    return new ApiKeyAuthentication(authUser, true, null);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authUser.roles().stream()
        .map(Enum::name)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
  }

  @Override
  public Object getCredentials() {
    return apiKey;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return authUser;
  }

  @Override
  public boolean isAuthenticated() {
    return authenticated;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getName() {
    return null;
  }
}
