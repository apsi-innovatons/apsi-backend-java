package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.innowacja.model.enums.IdeaStatus;

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
  private Integer subjectId;
  private Integer authorId;
  private Boolean anonymous;
  private Boolean blocked;
  private List<String> keywords;
  private List<CostDto> costs;
  private List<BenefitDto> benefits;
}
