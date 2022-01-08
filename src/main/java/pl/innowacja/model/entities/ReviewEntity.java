package pl.innowacja.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@Table(name = "Reviews", schema = "dbo")
public class ReviewEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "Description")
  private String description;
  @Column(name = "Rating", nullable = false)
  private Double rating;
  @Column(name = "Weight", nullable = false)
  private Double weight;
  @Column(name = "Date", nullable = false)
  private Date date;
  @Column(name = "IdeaId")
  private Integer ideaId;
  @Column(name = "AuthorId")
  private Integer authorId;
}
