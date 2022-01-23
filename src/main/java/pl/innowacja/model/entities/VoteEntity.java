package pl.innowacja.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "Vote", schema = "dbo")
@NoArgsConstructor
@AllArgsConstructor
public class VoteEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "Value", nullable = false)
  private Integer value;
  @Column(name = "CommitteeMemberId", nullable = false)
  private Integer committeeMemberId;
  @Column(name = "IdeaId")
  private Integer ideaId;
}
