package com.ihorpolataiko.springbootsecurityweb.controller.api;

import com.ihorpolataiko.springbootsecurityweb.security.jwt.JwtAuthService;
import com.ihorpolataiko.springbootsecurityweb.security.jwt.dto.LoginDto;
import com.ihorpolataiko.springbootsecurityweb.security.jwt.dto.TokenDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/jwt")
public class JwtAuthApiController {

  private final JwtAuthService jwtAuthService;

  public JwtAuthApiController(JwtAuthService jwtAuthService) {
    this.jwtAuthService = jwtAuthService;
  }

  @PreAuthorize("isAnonymous()")
  @PostMapping("/login")
  public TokenDto login(@RequestBody LoginDto loginDto) {

    return jwtAuthService.login(loginDto);
  }
}
