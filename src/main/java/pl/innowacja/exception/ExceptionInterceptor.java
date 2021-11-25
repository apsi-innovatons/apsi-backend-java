package pl.innowacja.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@ControllerAdvice
public class ExceptionInterceptor implements HandlerInterceptor {

  @ExceptionHandler({NoResourceFoundException.class})
  protected ResponseEntity<String> handleClientError(NoResourceFoundException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler({IdeaServiceException.class})
  protected ResponseEntity<String> handleServiceException(IdeaServiceException e) {
    return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
  }

  @ExceptionHandler({SecurityException.class})
  protected ResponseEntity<String> handleSecurityException(SecurityException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }
}
