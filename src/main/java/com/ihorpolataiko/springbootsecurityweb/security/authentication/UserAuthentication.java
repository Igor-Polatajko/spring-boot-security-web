package com.ihorpolataiko.springbootsecurityweb.security.authentication;

import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public record UserAuthentication(AuthUser authUser) implements Authentication {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authUser.roles().stream()
        .map(Enum::name)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
  }

  @Override
  public Object getCredentials() {
    return null;
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
    // This value is set to true in this example because Authentication is used only to represent
    // an authenticated user and not for transferring authentication details
    return true;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    // NoOp
  }

  @Override
  public String getName() {
    return null;
  }
}
