package com.ihorpolataiko.springbootsecurityweb.security;

import com.ihorpolataiko.springbootsecurityweb.common.Role;

public record AuthUser(String userId, Role role) {}
