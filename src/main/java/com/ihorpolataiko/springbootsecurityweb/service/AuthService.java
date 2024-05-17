package com.ihorpolataiko.springbootsecurityweb.service;

import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponse;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponseWithCredentials;
import com.ihorpolataiko.springbootsecurityweb.security.dto.LoginDto;
import com.ihorpolataiko.springbootsecurityweb.security.dto.TokenDto;
import com.ihorpolataiko.springbootsecurityweb.security.exception.ApplicationAuthenticationException;
import com.ihorpolataiko.springbootsecurityweb.security.service.jwt.JwtService;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  private final JwtService jwtService;

  public AuthService(
      UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public TokenDto login(LoginDto loginDto) {

    UserResponseWithCredentials userCredentials =
        userService.getUserCredentialsByUsername(loginDto.username());

    if (!passwordEncoder.matches(loginDto.password(), userCredentials.passwordHash())) {
      throw new ApplicationAuthenticationException("Password is incorrect");
    }

    UserResponse userResponse = userCredentials.userResponse();
    AuthUser authUser = new AuthUser(userResponse.id(), userResponse.roles());

    String jwtToken = jwtService.createJwtToken(authUser);

    return new TokenDto(jwtToken);
  }
}
