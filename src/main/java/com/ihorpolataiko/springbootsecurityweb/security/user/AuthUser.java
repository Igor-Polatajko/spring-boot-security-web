package com.ihorpolataiko.springbootsecurityweb.security.user;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public record AuthUser(String userId, List<Role> roles, String passwordHash) {

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
  }
}
