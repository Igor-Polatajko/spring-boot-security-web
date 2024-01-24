package com.ihorpolataiko.springbootsecurityweb.exception;

import com.ihorpolataiko.springbootsecurityweb.dto.error.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.ihorpolataiko.springbootsecurityweb.controller.api")
public class RestExceptionControllerAdvice {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiErrorResponse handleNotFoundException(NotFoundException ex) {
    return getApiErrorResponse(ex.getMessage());
  }

  @ExceptionHandler(NoAccessException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiErrorResponse handleNoAccessException(NoAccessException ex) {
    return getApiErrorResponse(ex.getMessage());
  }

  private ApiErrorResponse getApiErrorResponse(String ex) {
    return new ApiErrorResponse(ex);
  }
}
