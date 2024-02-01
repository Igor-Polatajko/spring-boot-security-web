package com.ihorpolataiko.springbootsecurityweb.controller.api;

import com.ihorpolataiko.springbootsecurityweb.common.OpenApiConstants;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserCreateRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserPasswordUpdateRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponse;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserUpdateRequest;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import com.ihorpolataiko.springbootsecurityweb.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserApiController {

  private final UserService userService;

  public UserApiController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("permitAll()")
  @PostMapping
  public UserResponse createUser(@RequestBody UserCreateRequest userCreateRequest) {

    return userService.createUser(userCreateRequest);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/change-password") // ToDo Ihor, make more compliant with REST API standard?
  @SecurityRequirement(name = OpenApiConstants.TOKEN_SECURITY_REQUIREMENT)
  public void changeUserPassword(
      @AuthenticationPrincipal AuthUser authUser,
      @RequestBody UserPasswordUpdateRequest passwordUpdateRequest) {
    userService.changeUserPassword(passwordUpdateRequest, authUser);
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping // ToDo Ihor, make more compliant with REST API standard?
  @SecurityRequirement(name = OpenApiConstants.TOKEN_SECURITY_REQUIREMENT)
  public UserResponse updateUser(
      @RequestBody UserUpdateRequest userUpdateRequest,
      @AuthenticationPrincipal AuthUser authUser) {
    return userService.updateUser(userUpdateRequest, authUser);
  }

  // check isAuthenticated(), because authentication.principal is String for anonymous
  // authentication
  // Spring SPEL uses same parameter names from as method parameter
  @PreAuthorize(
      "isAuthenticated() && (hasAuthority('ROLE_ADMIN') || (#userId == authentication.principal.userId))")
  @GetMapping("/{id}")
  @SecurityRequirement(name = OpenApiConstants.TOKEN_SECURITY_REQUIREMENT)
  public UserResponse getUserById(@PathVariable("id") String userId) {
    return userService.getUserById(userId);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  @SecurityRequirement(name = OpenApiConstants.TOKEN_SECURITY_REQUIREMENT)
  public Page<UserResponse> listUsers(Pageable pageable) {
    return userService.listUsers(pageable);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/{id}/activate")
  @SecurityRequirement(name = OpenApiConstants.TOKEN_SECURITY_REQUIREMENT)
  public UserResponse activateUser(@PathVariable("id") String userId) {
    return userService.activateUser(userId);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/{id}/deactivate")
  @SecurityRequirement(name = OpenApiConstants.TOKEN_SECURITY_REQUIREMENT)
  public UserResponse deactivateUser(@PathVariable("id") String userId) {
    return userService.deactivateUser(userId);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/{id}/promote")
  @SecurityRequirement(name = OpenApiConstants.TOKEN_SECURITY_REQUIREMENT)
  public UserResponse promoteUserToAdmin(@PathVariable("id") String userId) {
    return userService.promoteUserToAdmin(userId);
  }
}
