package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
  private Integer id;
  private Integer value;
  private Integer committeeMemberId;
  private Integer ideaId;
}
