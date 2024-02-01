package com.ihorpolataiko.springbootsecurityweb.security.exception;

import org.springframework.security.access.AccessDeniedException;

public class TokenAuthorizationException extends AccessDeniedException {
  public TokenAuthorizationException(String message) {
    super(message);
  }
}
