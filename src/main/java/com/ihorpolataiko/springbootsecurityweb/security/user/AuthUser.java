package com.ihorpolataiko.springbootsecurityweb.security.user;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// Now our user has to implement UserDetails, because that is required by UserDetailsService, which
// is used by BasicAuthenticationFilter under the hoods of Spring Security
// (BasicAuthenticationFilter -> ProviderManager -> DaoAuthenticationProvider -> UserDetailsService)
public record AuthUser(String userId, List<Role> roles, String passwordHash)
    implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
  }

  @Override
  public String getPassword() {
    return passwordHash;
  }

  @Override
  public String getUsername() {
    return null;
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
}
