package com.ihorpolataiko.springbootsecurityweb.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;

@EnableMethodSecurity // allow to specify access via annotations
@Configuration
public class SecurityConfig {

  private final AuthenticationEntryPoint authenticationEntryPoint;

  private final AccessDeniedHandler accessDeniedHandler;

  public SecurityConfig(
      AuthenticationEntryPoint authenticationEntryPoint, AccessDeniedHandler accessDeniedHandler) {
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.accessDeniedHandler = accessDeniedHandler;
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      @Qualifier("jwtAuthenticationFilter") AuthenticationFilter jwtAuthenticationFilter,
      @Qualifier("apiKeyAuthenticationFilter") AuthenticationFilter apiKeyAuthenticationFilter)
      throws Exception {

    http.addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class)
        .addFilterBefore(apiKeyAuthenticationFilter, AuthorizationFilter.class)
        .authorizeHttpRequests(
            mather ->
                mather
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/*",
                        "/v3/api-docs",
                        "/v3/api-docs/swagger-config")
                    .permitAll())
        .authorizeHttpRequests(
            matcher ->
                matcher
                    // method security will be evaluated after DSL configs,
                    // so we have to define public paths upfront
                    .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/users")
                    .permitAll())
        .authorizeHttpRequests(matcher -> matcher.anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(AbstractHttpConfigurer::disable)
        .exceptionHandling(
            customizer ->
                customizer
                    .accessDeniedHandler(accessDeniedHandler)
                    .authenticationEntryPoint(authenticationEntryPoint));

    return http.build();
  }

  //
  // You can subclass the AuthenticationFilter and define the details of its behavior in the
  // subclass.
  // Alternatively, you can create a new object(s) of
  // AuthenticationFilter (without subclassing it),
  // register it (them) as a bean(s), and configure the details of its
  // behaviors via the AuthenticationFilter's public methods.
  // In any case, our auth filters should be registered on HttpSecurity DSL
  // via the ".addFilterBefore(...)" method
  //
  //  @Bean
  //  AuthenticationFilter jwtAuthenticationFilter(
  //      AuthenticationManager authenticationManager,
  //      JwtAuthenticationConverter jwtAuthenticationConverter,
  //      AuthenticationEntryPoint authenticationEntryPoint) {
  //
  //    AuthenticationFilter authenticationFilter =
  //        new AuthenticationFilter(authenticationManager, jwtAuthenticationConverter);
  //
  //    authenticationFilter.setSuccessHandler((request, response, authentication) -> {});
  //    authenticationFilter.setFailureHandler(authenticationEntryPoint::commence);
  //
  //    return authenticationFilter;
  //  }
  //
  //  @Bean
  //  AuthenticationFilter apiKeyAuthenticationFilter(
  //      AuthenticationManager authenticationManager,
  //      ApiKeyAuthenticationConverter apiKeyAuthenticationConverter,
  //      AuthenticationEntryPoint authenticationEntryPoint) {
  //
  //    AuthenticationFilter authenticationFilter =
  //        new AuthenticationFilter(authenticationManager, apiKeyAuthenticationConverter);
  //
  //    authenticationFilter.setSuccessHandler((request, response, authentication) -> {});
  //    authenticationFilter.setFailureHandler(authenticationEntryPoint::commence);
  //
  //    return authenticationFilter;
  //  }
}
