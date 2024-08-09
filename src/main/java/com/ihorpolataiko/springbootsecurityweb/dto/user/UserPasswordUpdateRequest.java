package com.ihorpolataiko.springbootsecurityweb.dto.user;

public record UserPasswordUpdateRequest(String oldPassword, String newPassword) {}
