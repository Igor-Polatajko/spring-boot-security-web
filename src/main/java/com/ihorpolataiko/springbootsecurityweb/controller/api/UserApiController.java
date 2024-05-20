package com.ihorpolataiko.springbootsecurityweb.controller.api;

import com.ihorpolataiko.springbootsecurityweb.common.OpenApiConstants;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserCreateRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserPasswordUpdateRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponse;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserUpdateRequest;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import com.ihorpolataiko.springbootsecurityweb.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
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
  @PostMapping("/change-password")
  @SecurityRequirement(name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT)
  public void changeUserPassword(
      @AuthenticationPrincipal AuthUser authUser,
      @RequestBody UserPasswordUpdateRequest passwordUpdateRequest) {
    userService.changeUserPassword(passwordUpdateRequest, authUser);
  }

  @PreAuthorize("isAuthenticated() && #userId == authentication.principal.userId")
  @PutMapping("/{id}")
  @SecurityRequirement(name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT)
  public UserResponse updateUser(
      @PathVariable("id") String userId, @RequestBody UserUpdateRequest userUpdateRequest) {
    return userService.updateUser(userId, userUpdateRequest);
  }

  // check isAuthenticated(), because authentication.principal is String for anonymous
  // authentication
  // Spring SPEL uses the same parameter names as method parameter names.
  // Yes, there is a /me endpoint, but we also allow users to get user data by ID just for
  // demonstration purposes
  @PreAuthorize(
      "isAuthenticated() && (hasAuthority('ROLE_ADMIN') || (#userId == authentication.principal.userId))")
  @GetMapping("/{id}")
  @SecurityRequirement(name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT)
  public UserResponse getUserById(@PathVariable("id") String userId) {
    return userService.getUserById(userId);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/me")
  @SecurityRequirement(name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT)
  public UserResponse getCurrentUser(@AuthenticationPrincipal AuthUser authUser) {
    return userService.getUserById(authUser.userId());
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping
  @SecurityRequirement(name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT)
  public Page<UserResponse> listUsers(@PageableDefault @ParameterObject Pageable pageable) {
    return userService.listUsers(pageable);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/{id}/activate")
  @SecurityRequirement(name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT)
  public UserResponse activateUser(@PathVariable("id") String userId) {
    return userService.activateUser(userId);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/{id}/deactivate")
  @SecurityRequirement(name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT)
  public UserResponse deactivateUser(@PathVariable("id") String userId) {
    return userService.deactivateUser(userId);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/{id}/promote")
  @SecurityRequirement(name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT)
  public UserResponse promoteUserToAdmin(@PathVariable("id") String userId) {
    return userService.promoteUserToAdmin(userId);
  }
}
