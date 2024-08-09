package com.ihorpolataiko.springbootsecurityweb.security.configurer;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationManagerEventListenersConfigurer
    extends AbstractHttpConfigurer<AuthenticationManagerEventListenersConfigurer, HttpSecurity> {

  private final ApplicationEventPublisher applicationEventPublisher;

  public AuthenticationManagerEventListenersConfigurer(
      ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void configure(HttpSecurity http) {

    AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

    if (authenticationManager instanceof ProviderManager) {
      ((ProviderManager) authenticationManager)
          .setAuthenticationEventPublisher(
              new DefaultAuthenticationEventPublisher(applicationEventPublisher));
    }
  }
}
