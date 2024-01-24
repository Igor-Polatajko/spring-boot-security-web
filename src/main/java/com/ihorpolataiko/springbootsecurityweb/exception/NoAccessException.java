package com.ihorpolataiko.springbootsecurityweb.exception;

public class NoAccessException extends RuntimeException {

  public NoAccessException() {
    super("No access");
  }
}
