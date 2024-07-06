package com.ihorpolataiko.springbootsecurityweb.security.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

// These events are produced by ProviderManager
@Component
@Slf4j
public class SecurityEventsListener {

  @EventListener(AuthenticationSuccessEvent.class)
  public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
    log.info(
        "Authentication SUCCESS event received. Authentication: {}", event.getAuthentication());
  }

  @EventListener(AbstractAuthenticationFailureEvent.class)
  public void handleAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
    log.warn(
        "Authentication FAILURE event received. Authentication: {}", event.getAuthentication());
  }
}
