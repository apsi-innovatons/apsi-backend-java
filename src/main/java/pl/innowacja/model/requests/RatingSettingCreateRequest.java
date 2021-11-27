package pl.innowacja.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.innowacja.model.enums.UserRole;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingSettingCreateRequest {
  @Valid
  private List<RatingSetting> ratingSettings;

  @Data
  @NoArgsConstructor
  public static class RatingSetting {
    private UserRole userRole;
    @DecimalMin("0")
    @DecimalMax("5")
    private Double weight;
  }
}
