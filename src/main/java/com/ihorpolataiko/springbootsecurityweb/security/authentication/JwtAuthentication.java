package com.ihorpolataiko.springbootsecurityweb.security.authentication;

import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// Let's extend AbstractAuthenticationToken this time, so we will need to override fewer methods
public class JwtAuthentication extends AbstractAuthenticationToken {

  private final AuthUser authUser;

  private final String jwtToken;

  private JwtAuthentication(
      Collection<? extends GrantedAuthority> authorities,
      AuthUser authUser,
      boolean authenticated,
      String jwtToken) {

    super(authorities);
    super.setAuthenticated(authenticated);

    this.authUser = authUser;
    this.jwtToken = jwtToken;
  }

  public static JwtAuthentication unauthenticated(String jwtToken) {

    return new JwtAuthentication(null, null, false, jwtToken);
  }

  public static JwtAuthentication authenticated(AuthUser authUser) {

    Set<SimpleGrantedAuthority> authorities =
        authUser.roles().stream()
            .map(Enum::name)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

    return new JwtAuthentication(authorities, authUser, true, null);
  }

  @Override
  public String getCredentials() {
    return jwtToken;
  }

  @Override
  public Object getPrincipal() {
    return authUser;
  }
}
