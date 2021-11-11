package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CostDto {
  private Integer id;
  private String title;
  private Double value;
}
