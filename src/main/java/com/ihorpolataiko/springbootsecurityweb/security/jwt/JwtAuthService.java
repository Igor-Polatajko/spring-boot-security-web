package com.ihorpolataiko.springbootsecurityweb.security.jwt;

import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponse;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponseWithCredentials;
import com.ihorpolataiko.springbootsecurityweb.security.exception.ApplicationAuthenticationException;
import com.ihorpolataiko.springbootsecurityweb.security.jwt.dto.LoginDto;
import com.ihorpolataiko.springbootsecurityweb.security.jwt.dto.TokenDto;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUserType;
import com.ihorpolataiko.springbootsecurityweb.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthService {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  private final JwtService jwtService;

  public JwtAuthService(
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
    AuthUser authUser =
        new AuthUser(
            userResponse.id(),
            userResponse.username(),
            userResponse.roles(),
            null,
            AuthUserType.INTERNAL);

    String jwtToken = jwtService.createJwtToken(authUser);

    return new TokenDto(jwtToken);
  }
}
