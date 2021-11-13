package pl.innowacja.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class IdeaServiceException extends RuntimeException {
  private final HttpStatus httpStatus;

  public IdeaServiceException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }
}
