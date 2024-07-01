package com.ihorpolataiko.springbootsecurityweb.exception;

import org.springframework.security.access.AccessDeniedException;

public class NoAccessException extends AccessDeniedException {

  public NoAccessException() {
    super("No access");
  }
}
