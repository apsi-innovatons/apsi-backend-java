package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BenefitDto {
  private Integer id;
  private String title;
  private String description;
}
