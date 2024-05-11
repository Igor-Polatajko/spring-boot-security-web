package com.ihorpolataiko.springbootsecurityweb.security.user;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

// Decentralized cache (e.g. Redis) or key-value database will be, most likely, used in production
// scenario
@Component
public class AuthUserCache {

  // You may want to use some collection with time-to-leave to handle session expiration
  private final Map<String, AuthUser> sessions = new ConcurrentHashMap<>();

  public void login(String token, AuthUser authUser) {
    sessions.put(token, authUser);
  }

  public void logout(String token) {
    sessions.remove(token);
  }

  public Optional<AuthUser> getByToken(String token) {
    return Optional.ofNullable(sessions.get(token));
  }
}
