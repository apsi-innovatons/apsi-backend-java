package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.DecimalMin;

@Getter
@AllArgsConstructor
public class CostDto {
  private Integer id;
  private String title;
  @DecimalMin("0")
  private Double value;
}
