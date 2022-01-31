package pl.innowacja.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectUserEntityPK implements Serializable {
  @Column(name = "CommitteeMembersId", nullable = false)
  @Id
  private Integer committeeMembersId;

  @Column(name = "SubjectsId", nullable = false)
  @Id
  private Integer subjectsId;
}
