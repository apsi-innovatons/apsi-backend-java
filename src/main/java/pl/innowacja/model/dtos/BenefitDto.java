package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class BenefitDto {
  private Integer id;
  @NotBlank
  private String title;
  private String description;
}
