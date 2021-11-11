package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.innowacja.model.enums.UserRole;

@Data
@AllArgsConstructor
public class SubjectDto {
  private Integer id;
  private String name;
  private UserRole audience;
}
