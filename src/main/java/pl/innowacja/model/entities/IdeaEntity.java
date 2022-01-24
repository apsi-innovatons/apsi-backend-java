package pl.innowacja.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Ideas", schema = "dbo")
public class IdeaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "Title")
  private String title;
  @Column(name = "Description")
  private String description;
  @Column(name = "Status", nullable = false)
  private Integer status;
  @Column(name = "StatusDescription")
  private String statusDescription;
  @Column(name = "Date", nullable = false)
  private Date date;
  @Column(name = "Blocked", nullable = false)
  private Boolean blocked;
  @Column(name = "Anonymous", nullable = false)
  private Boolean anonymous;
  @Column(name = "Rating", nullable = false)
  private Double rating;
  @Column(name = "SubjectId")
  private Integer subjectId;
  @Column(name = "AuthorId")
  private Integer authorId;
  @Column(name = "Keywords")
  private String keywords;
  @Column(name = "VotesSum", nullable = false)
  private Integer votesSum;
  @Column(name = "RejectsSum", nullable = false)
  private Integer rejectsSum;
}
