package com.ihorpolataiko.springbootsecurityweb.security.form;

import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponseWithCredentials;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUserType;
import com.ihorpolataiko.springbootsecurityweb.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserService userService;

  public CustomUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserResponseWithCredentials userCredentialsByUsername =
        userService.getUserCredentialsByUsername(username);

    return new AuthUser(
        userCredentialsByUsername.userResponse().id(),
        userCredentialsByUsername.userResponse().username(),
        userCredentialsByUsername.userResponse().roles(),
        userCredentialsByUsername.passwordHash(),
        AuthUserType.INTERNAL);
  }
}
