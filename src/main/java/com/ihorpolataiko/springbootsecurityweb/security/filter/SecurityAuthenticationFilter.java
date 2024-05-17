package com.ihorpolataiko.springbootsecurityweb.security.filter;

import com.ihorpolataiko.springbootsecurityweb.common.AuthConstants;
import com.ihorpolataiko.springbootsecurityweb.security.authentication.UserAuthentication;
import com.ihorpolataiko.springbootsecurityweb.security.exception.TokenAuthenticationException;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUserCache;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SecurityAuthenticationFilter extends OncePerRequestFilter {

  private final AuthUserCache authUserCache;

  public SecurityAuthenticationFilter(AuthUserCache authUserCache) {
    this.authUserCache = authUserCache;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authenticationHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);

    if (authenticationHeader == null) {
      // Authentication token is not present, let's rely on anonymous authentication
      filterChain.doFilter(request, response);
      return;
    }

    AuthUser authUser =
        authUserCache
            .getByToken(authenticationHeader)
            .orElseThrow(() -> new TokenAuthenticationException("Token is not valid"));

    SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(authUser));

    filterChain.doFilter(request, response);
  }
}
