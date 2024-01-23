package com.ihorpolataiko.springbootsecurityweb.dto.user;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import java.util.List;

public record UserResponse(
    String id,
    String username,
    String firstName,
    String lastName,
    List<Role> roles,
    Boolean active) {}
