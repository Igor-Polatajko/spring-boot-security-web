package com.ihorpolataiko.springbootsecurityweb.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// Usually, Spring Security implementations do not allocate a separate filter
// to performing authentication itself (aka calling AuthenticationManager.authenticate(...))
// and make calls to AuthenticationManager in the same filter where unauthenticated
// authentication is created, but in this example let's dedicate a separate filter solely for
// authentication of unauthenticated Authentication.
// This filter should be registered in the chain after the filters
// that create unauthenticated authentication.
@Component
public class SecurityAuthenticationFilter extends OncePerRequestFilter {

  private AuthenticationManager authenticationManager;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    Authentication unauthenticatedAuthentication =
        SecurityContextHolder.getContext().getAuthentication();

    if (unauthenticatedAuthentication == null || unauthenticatedAuthentication.isAuthenticated()) {

      filterChain.doFilter(request, response);
      return;
    }

    Authentication authenticatedAuthentication =
        authenticationManager.authenticate(unauthenticatedAuthentication);

    if (authenticatedAuthentication != null) {

      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(authenticatedAuthentication);
      SecurityContextHolder.setContext(securityContext);
    }

    filterChain.doFilter(request, response);
  }

  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }
}
