package com.ihorpolataiko.springbootsecurityweb.config;

import com.ihorpolataiko.springbootsecurityweb.security.filter.ApiKeyAuthenticationFilter;
import com.ihorpolataiko.springbootsecurityweb.security.filter.JwtAuthenticationFilter;
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

@EnableMethodSecurity // allow to specify access via annotations
@Configuration
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  private final ApiKeyAuthenticationFilter apiKeyAuthenticationFilter;

  private final AuthenticationEntryPoint authenticationEntryPoint;

  private final AccessDeniedHandler accessDeniedHandler;

  public SecurityConfig(
      JwtAuthenticationFilter jwtAuthenticationFilter,
      ApiKeyAuthenticationFilter apiKeyAuthenticationFilter,
      AuthenticationEntryPoint authenticationEntryPoint,
      AccessDeniedHandler accessDeniedHandler) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.apiKeyAuthenticationFilter = apiKeyAuthenticationFilter;
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.accessDeniedHandler = accessDeniedHandler;
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

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
  //  If you need just one authentication filter,
  //  you may simpy register a bean of AuthenticationFilter type
  //
  //  @Bean
  //  AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager,
  // AuthenticationConverter authenticationConverter,
  // AuthenticationEntryPoint authenticationEntryPoint) {
  //
  //    AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager,
  // authenticationConverter);
  //
  //    authenticationFilter.setSuccessHandler((request, response, authentication) -> {});
  //
  //    setFailureHandler(authenticationEntryPoint::commence);
  //
  //    return authenticationFilter;
  //  }
}
