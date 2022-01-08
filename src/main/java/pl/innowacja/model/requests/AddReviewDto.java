package pl.innowacja.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewDto {
  private String description;
  @NotNull
  @DecimalMin("0")
  @DecimalMax("6")
  private Double rating;
}
