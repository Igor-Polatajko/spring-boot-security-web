package com.ihorpolataiko.springbootsecurityweb.config;

import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;

// Let's define the authentication manager in another config in order not to create a circular
// dependency
@Configuration
public class AuthenticationManagerConfig {

  // Inject all our implementations of the AuthenticationProvider interface as parameter of this
  // method
  @Bean
  public AuthenticationManager authenticationManager(
      List<AuthenticationProvider> authenticationProviders,
      ApplicationEventPublisher applicationEventPublisher) {

    // Using out-of-the-box implementation of authentication manager
    ProviderManager providerManager = new ProviderManager(authenticationProviders);

    // Also, let's set the authentication event publisher so that we can receive
    // events on successful and failed authentication attempts
    // It's completely optional: if we do not set the publisher
    // it will default to NullEventPublisher implementation that does not publish events
    providerManager.setAuthenticationEventPublisher(
        new DefaultAuthenticationEventPublisher(applicationEventPublisher));

    return providerManager;
  }
}
