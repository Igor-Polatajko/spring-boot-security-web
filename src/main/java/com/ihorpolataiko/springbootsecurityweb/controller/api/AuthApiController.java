package com.ihorpolataiko.springbootsecurityweb.controller.api;

import com.ihorpolataiko.springbootsecurityweb.security.dto.LoginDto;
import com.ihorpolataiko.springbootsecurityweb.security.dto.TokenDto;
import com.ihorpolataiko.springbootsecurityweb.service.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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
}
