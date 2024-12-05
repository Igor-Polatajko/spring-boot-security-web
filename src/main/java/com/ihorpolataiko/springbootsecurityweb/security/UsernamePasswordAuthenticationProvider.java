package com.ihorpolataiko.springbootsecurityweb.security;

import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponseWithCredentials;
import com.ihorpolataiko.springbootsecurityweb.security.exception.ApplicationAuthenticationException;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import com.ihorpolataiko.springbootsecurityweb.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

  // We are free to implement any authentication logic we want.
  // In our case, we use our existing UserService to load the user data by username, and then we
  // will check if the password, provided in the request, matches the hash of the password from the
  // database
  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  public UsernamePasswordAuthenticationProvider(
      UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    UserResponseWithCredentials userCredentialsByUsername =
        userService.getUserCredentialsByUsername(authentication.getName());

    if (!passwordEncoder.matches(
        authentication.getCredentials().toString(), userCredentialsByUsername.passwordHash())) {
      throw new ApplicationAuthenticationException("Bad credentials");
    }

    AuthUser authUser =
        new AuthUser(
            userCredentialsByUsername.userResponse().id(),
            userCredentialsByUsername.userResponse().roles(),
            userCredentialsByUsername.passwordHash());

    // if null would be returned, then another implementation of authentication provider,
    // that support given type of the authentication will be invoked
    return new UsernamePasswordAuthenticationToken(
        authUser, authentication.getCredentials(), authUser.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {

    // UsernamePasswordAuthenticationToken is the implementation of the Authentication,
    // created by UsernamePasswordAuthenticationFilter which is registered
    // in Spring Security when we configure .formLogin() via HttpSecurity DSL
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}