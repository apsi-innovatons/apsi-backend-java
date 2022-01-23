package pl.innowacja.services;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtil {
  public static Integer getCurrentUserId() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    return (int) authentication.getCredentials();
  }
}
