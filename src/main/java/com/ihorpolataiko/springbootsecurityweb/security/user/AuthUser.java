package com.ihorpolataiko.springbootsecurityweb.security.user;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record AuthUser(
    String userId,
    String username,
    List<Role> roles,
    @Nullable String passwordHash,
    AuthUserType authUserType)
    implements OAuth2User, UserDetails {

  @Override
  public Map<String, Object> getAttributes() {
    return Map.of();
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
  }

  @Override
  public String getPassword() {
    return passwordHash;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getName() {
    return userId;
  }

  public boolean isInternalUser() {
    return authUserType == AuthUserType.INTERNAL;
  }
}
