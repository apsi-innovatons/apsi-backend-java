package pl.innowacja.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.innowacja.model.enums.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingSettingCreateRequest {
  private UserRole userRole;
  private Double weight;
}
