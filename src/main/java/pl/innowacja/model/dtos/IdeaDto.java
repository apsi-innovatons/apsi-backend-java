package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.innowacja.model.enums.IdeaStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdeaDto {
  private Integer id;
  private String title;
  private String description;
  private IdeaStatus status;
  private String statusDescription;
  private LocalDate date;
  private Double rating;
  private Integer subjectId;
  private Integer authorId;
  @NotNull
  private Boolean anonymous;
  @NotNull
  private Boolean blocked;
  private List<String> keywords;
  @Valid
  private List<CostDto> costs;
  @Valid
  private List<BenefitDto> benefits;
}
