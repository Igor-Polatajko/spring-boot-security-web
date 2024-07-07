package com.ihorpolataiko.springbootsecurityweb.security.configurer;

import com.ihorpolataiko.springbootsecurityweb.security.filter.ApiKeyFilter;
import com.ihorpolataiko.springbootsecurityweb.security.filter.JwtTokenFilter;
import com.ihorpolataiko.springbootsecurityweb.security.filter.SecurityAuthenticationFilter;
import com.ihorpolataiko.springbootsecurityweb.security.provider.ApiKeyAuthenticationProvider;
import com.ihorpolataiko.springbootsecurityweb.security.provider.JwtAuthenticationProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.stereotype.Component;

@Component
public class CustomSecurityConfigurer
    extends AbstractHttpConfigurer<CustomSecurityConfigurer, HttpSecurity> {

  private final JwtTokenFilter jwtTokenFilter;

  private final ApiKeyFilter apiKeyFilter;

  private final SecurityAuthenticationFilter securityAuthenticationFilter;

  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  private final ApiKeyAuthenticationProvider apiKeyAuthenticationProvider;

  private final ApplicationEventPublisher applicationEventPublisher;

  public CustomSecurityConfigurer(
      JwtTokenFilter jwtTokenFilter,
      ApiKeyFilter apiKeyFilter,
      SecurityAuthenticationFilter securityAuthenticationFilter,
      JwtAuthenticationProvider jwtAuthenticationProvider,
      ApiKeyAuthenticationProvider apiKeyAuthenticationProvider,
      ApplicationEventPublisher applicationEventPublisher) {
    this.jwtTokenFilter = jwtTokenFilter;
    this.apiKeyFilter = apiKeyFilter;
    this.securityAuthenticationFilter = securityAuthenticationFilter;
    this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    this.apiKeyAuthenticationProvider = apiKeyAuthenticationProvider;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void init(HttpSecurity http) {

    http.addFilterBefore(securityAuthenticationFilter, AuthorizationFilter.class)
        .addFilterBefore(jwtTokenFilter, SecurityAuthenticationFilter.class)
        .addFilterBefore(apiKeyFilter, JwtTokenFilter.class)
        .authenticationProvider(jwtAuthenticationProvider)
        .authenticationProvider(apiKeyAuthenticationProvider);
  }

  // Because we write this code in the configurer, ProviderManager manager
  // containing authentication providers registered by us on HttpSecurity DSL will be already
  // created and accessible
  // at the point when HttpSecurity invokes our configurer [it's created just before .configure()
  // methods are invoked on the configurers]
  @Override
  public void configure(HttpSecurity http) {

    AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

    configureEventPublisher(authenticationManager);

    securityAuthenticationFilter.setAuthenticationManager(authenticationManager);
  }

  // Also, let's set the authentication event publisher so that we can receive
  // events on successful and failed authentication attempts
  // It's completely optional: if we do not set the publisher
  // it will default to NullEventPublisher implementation that does not publish events
  private void configureEventPublisher(AuthenticationManager authenticationManager) {

    if (authenticationManager instanceof ProviderManager) {
      ((ProviderManager) authenticationManager)
          .setAuthenticationEventPublisher(
              new DefaultAuthenticationEventPublisher(applicationEventPublisher));
    }
  }
}
