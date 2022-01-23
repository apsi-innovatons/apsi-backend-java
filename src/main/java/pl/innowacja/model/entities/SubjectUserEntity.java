package pl.innowacja.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "SubjectUser", schema = "dbo")
@IdClass(SubjectUserEntityPK.class)
@AllArgsConstructor
@NoArgsConstructor
public class SubjectUserEntity {
  @Id
  @Column(name = "CommitteeMembersId", nullable = false)
  private Integer committeeMembersId;

  @Id
  @Column(name = "SubjectsId", nullable = false)
  private Integer subjectsId;

}
