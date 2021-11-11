package pl.innowacja.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRole {
  Student(0),
  Employee(1),
  Committee(2),
  Admin(3);

  private final Integer value;

  public static UserRole valueOf(Integer value) {
    return Arrays.stream(values())
        .filter(role -> role.getValue().equals(value))
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException(String.format("No UserRole with integer value %d exists.", value)));
  }
}
