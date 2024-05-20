package com.ihorpolataiko.springbootsecurityweb.controller.api;

import com.ihorpolataiko.springbootsecurityweb.common.OpenApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/me")
  @SecurityRequirement(name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT)
  public User getCurrentUser(@AuthenticationPrincipal User user) {
    return user;
  }
}
