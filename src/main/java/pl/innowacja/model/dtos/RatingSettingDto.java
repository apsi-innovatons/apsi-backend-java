package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.innowacja.model.enums.UserRole;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingSettingDto {
  private Integer id;
  private UserRole userRole;
  @DecimalMin("0")
  @DecimalMax("5")
  private Double weight;
  private Integer ideaId;
}
