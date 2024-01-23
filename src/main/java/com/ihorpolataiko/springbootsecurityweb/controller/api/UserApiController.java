package com.ihorpolataiko.springbootsecurityweb.controller.api;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserCreateRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserPasswordUpdateRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponse;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserUpdateRequest;
import com.ihorpolataiko.springbootsecurityweb.security.AuthUser;
import com.ihorpolataiko.springbootsecurityweb.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserApiController {

  private final UserService userService;

  public UserApiController(UserService userService) {
    this.userService = userService;
  }

  // Public
  @PostMapping
  public UserResponse createUser(UserCreateRequest userCreateRequest) {

    return userService.createUser(userCreateRequest);
  }

  // Authenticated
  @PostMapping("/change-password") // ToDo Ihor, make more compliant with REST API standard?
  public void changeUserPassword(UserPasswordUpdateRequest passwordUpdateRequest) {
    userService.changeUserPassword(passwordUpdateRequest, new AuthUser("id", Role.USER));
  }

  // Authenticated
  @PutMapping // ToDo Ihor, make more compliant with REST API standard?
  public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
    return userService.updateUser(userUpdateRequest, new AuthUser("id", Role.USER));
  }

  // Authenticated
  // ToDo check on security level or controller level, that id of the user matches the id in the url
  //  or the role is admin
  @GetMapping("/{id}")
  public UserResponse getUser(@PathVariable("id") String userId) {
    return userService.getUser(userId);
  }

  // Admin
  @GetMapping
  public Page<UserResponse> listUsers(Pageable pageable) {
    return userService.listUsers(pageable);
  }

  // Admin
  @PostMapping("/{id}/activate")
  public UserResponse activateUser(@PathVariable("id") String userId) {
    return userService.activateUser(userId);
  }

  // Admin
  @PostMapping("/{id}/deactivate")
  public UserResponse deactivateUser(@PathVariable("id") String userId) {
    return userService.deactivateUser(userId);
  }

  // Admin
  @PostMapping("/{id}/promote")
  public UserResponse promoteUserToAdmin(@PathVariable("id") String userId) {
    return userService.promoteUserToAdmin(userId);
  }
}
