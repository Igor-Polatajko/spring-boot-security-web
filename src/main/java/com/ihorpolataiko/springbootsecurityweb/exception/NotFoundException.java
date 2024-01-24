package com.ihorpolataiko.springbootsecurityweb.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException() {
    super("Not found");
  }
}
