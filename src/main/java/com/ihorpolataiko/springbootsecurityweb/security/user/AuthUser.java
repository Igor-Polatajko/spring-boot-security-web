package com.ihorpolataiko.springbootsecurityweb.security.user;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record AuthUser(String userId, List<Role> roles) implements OAuth2User {

  @Override
  public Map<String, Object> getAttributes() {
    return Map.of();
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
  }

  @Override
  public String getName() {
    return userId;
  }
}
