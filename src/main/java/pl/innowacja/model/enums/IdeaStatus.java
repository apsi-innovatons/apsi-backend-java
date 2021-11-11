package pl.innowacja.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum IdeaStatus {
  New(0),
  Accepted(1),
  Rejected(2),
  ReuqestForDetails(3),
  PutAway(4);

  private final Integer value;

  public static IdeaStatus valueOf(Integer value) {
    return Arrays.stream(values())
        .filter(status -> status.value.equals(value))
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException(String.format("No IdeaStatus with integer value %d exists.", value)));
  }
}
