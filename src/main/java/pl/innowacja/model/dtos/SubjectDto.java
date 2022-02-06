package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.innowacja.model.enums.UserRole;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {
  private Integer id;
  private String name;
  private Boolean done;
  private Boolean alreadyVoted;
  private UserRole audience;
  private List<Integer> committeeMembers;
}
