package com.ihorpolataiko.springbootsecurityweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity // allow to specify access via annotations
@Configuration
public class SecurityConfig {

  private final String adminUsername;

  private final String adminPassword;

  public SecurityConfig(
      // let's just reuse the values, that we defined for our admin through the properties
      @Value("${admin.default.username}") String adminUsername,
      @Value("${admin.default.password}") String adminPassword) {

    this.adminUsername = adminUsername;
    this.adminPassword = adminPassword;
  }

  // we autowire AuthenticationManagerBuilder and register out-of-the-box implementation of
  // UserDetailsService there
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser(adminUsername)
        // we add prefix {noop}, because the password is not hashed (raw)
        // also, we do not register password encoder, so default is used
        .password("{noop}" + adminPassword)
        .roles("ADMIN"); // role without "ROLE_" prefix
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(
            mather ->
                mather
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/*",
                        "/v3/api-docs",
                        "/v3/api-docs/swagger-config")
                    .permitAll())
        .authorizeHttpRequests(matcher -> matcher.anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }
}
