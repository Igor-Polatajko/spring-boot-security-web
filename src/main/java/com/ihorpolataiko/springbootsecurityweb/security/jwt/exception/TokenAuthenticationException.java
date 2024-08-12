package com.ihorpolataiko.springbootsecurityweb.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenAuthenticationException extends AuthenticationException {
  public TokenAuthenticationException(String message) {
    super(message);
  }
}
