package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.innowacja.model.enums.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingSettingDto {
  private Integer id;
  private UserRole userRole;
  private Double weight;
  private Integer ideaId;
}
