package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdeaAttachmentDto {
  private Integer id;
  private Integer ideaId;
}
