package com.ihorpolataiko.springbootsecurityweb.security;

import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponseWithCredentials;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import com.ihorpolataiko.springbootsecurityweb.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

  // We are free to implement any authentication logic we want.
  // In our case we use our exiting UserService
  private final UserService userService;

  public CustomAuthenticationManager(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    UserResponseWithCredentials userCredentialsByUsername =
        userService.getUserCredentialsByUsername(authentication.getName());

    AuthUser authUser =
        new AuthUser(
            userCredentialsByUsername.userResponse().id(),
            userCredentialsByUsername.userResponse().roles(),
            userCredentialsByUsername.passwordHash());

    return new UsernamePasswordAuthenticationToken(
        authUser, authentication.getCredentials(), authUser.getAuthorities());
  }
}
