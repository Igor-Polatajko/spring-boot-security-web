package com.ihorpolataiko.springbootsecurityweb.exception;

import com.ihorpolataiko.springbootsecurityweb.dto.error.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.ihorpolataiko.springbootsecurityweb.controller.api")
public class RestExceptionControllerAdvice {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiErrorResponse handleNotFoundException(NotFoundException ex) {
    return getApiErrorResponse(ex);
  }

  // Spring Security method security (e.g., @PreAuthorize) is handled on the level of Spring
  // interceptors, which means exception will be handled by ControllerAdvice
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
    return getApiErrorResponse(ex);
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiErrorResponse handleAuthenticationException(AuthenticationException ex) {
    return getApiErrorResponse(ex);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiErrorResponse unhandledException(Exception ex) {
    return getApiErrorResponse(ex);
  }

  private ApiErrorResponse getApiErrorResponse(Exception ex) {

    log.error("Exception occurred", ex);

    return new ApiErrorResponse(ex.getMessage());
  }
}
