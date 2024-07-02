package com.ihorpolataiko.springbootsecurityweb.config;

import com.ihorpolataiko.springbootsecurityweb.security.CustomAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity // allow to specify access via annotations
@Configuration
public class SecurityConfig {

  private final CustomAuthenticationManager customAuthenticationManager;

  public SecurityConfig(CustomAuthenticationManager customAuthenticationManager) {
    this.customAuthenticationManager = customAuthenticationManager;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.formLogin(Customizer.withDefaults())
        // provide authentication manager implementation
        .authenticationManager(customAuthenticationManager)
        // allow public access to the home page
        .authorizeHttpRequests(mather -> mather.requestMatchers("/").permitAll())
        // deny requests to API for this example
        .authorizeHttpRequests(mather -> mather.requestMatchers("/api/*").denyAll())
        // deny requests to Swagger UI
        .authorizeHttpRequests(
            mather ->
                mather
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/*",
                        "/v3/api-docs",
                        "/v3/api-docs/swagger-config")
                    .denyAll())
        .authorizeHttpRequests(matcher -> matcher.anyRequest().authenticated())
        .exceptionHandling(customizer -> customizer.accessDeniedPage("/no-access"));

    return http.build();
  }
}
