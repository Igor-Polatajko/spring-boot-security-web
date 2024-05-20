package com.ihorpolataiko.springbootsecurityweb.controller.api;

import com.ihorpolataiko.springbootsecurityweb.common.AuthConstants;
import com.ihorpolataiko.springbootsecurityweb.common.OpenApiConstants;
import com.ihorpolataiko.springbootsecurityweb.security.dto.LoginDto;
import com.ihorpolataiko.springbootsecurityweb.security.dto.TokenDto;
import com.ihorpolataiko.springbootsecurityweb.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

  private final AuthService authService;

  public AuthApiController(AuthService authService) {
    this.authService = authService;
  }

  @PreAuthorize("isAnonymous()")
  @PostMapping("/login")
  public TokenDto login(@RequestBody LoginDto loginDto) {

    return authService.login(loginDto);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/logout")
  @SecurityRequirement(name = OpenApiConstants.TOKEN_SECURITY_REQUIREMENT)
  public void logout(HttpServletRequest httpServletRequest) {

    String token =
        Optional.ofNullable(httpServletRequest.getHeader(AuthConstants.AUTHORIZATION_HEADER))
            .orElseThrow();

    authService.logout(token);
  }
}
