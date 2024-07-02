package com.ihorpolataiko.springbootsecurityweb.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

// Let's define the authentication manager in another config in order not to create a circular
// dependency
@Configuration
public class AuthenticationManagerConfig {

  // Inject all our implementations of the AuthenticationProvider interface as parameter of this
  // method
  @Bean
  public AuthenticationManager authenticationManager(
      List<AuthenticationProvider> authenticationProviders) {

    // Using out-of-the-box implementation of authentication manager
    return new ProviderManager(authenticationProviders);
  }
}
