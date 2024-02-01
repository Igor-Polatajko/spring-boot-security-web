package com.ihorpolataiko.springbootsecurityweb.dto.user;

public record UserResponseWithCredentials(UserResponse userResponse, String passwordHash) {}
