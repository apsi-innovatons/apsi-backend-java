package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.innowacja.model.enums.IdeaStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionDto {
  private IdeaStatus ideaStatus;
  private String description;
}
