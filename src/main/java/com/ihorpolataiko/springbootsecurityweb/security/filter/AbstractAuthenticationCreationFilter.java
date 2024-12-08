package com.ihorpolataiko.springbootsecurityweb.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public abstract class AbstractAuthenticationCreationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    Authentication builtAuthentication = buildAuthentication(request);

    if (builtAuthentication != null) {

      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(builtAuthentication);
      SecurityContextHolder.setContext(securityContext);
    }

    filterChain.doFilter(request, response);
  }

  protected abstract Authentication buildAuthentication(HttpServletRequest request);
}
