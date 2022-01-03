package pl.innowacja.model.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Ratings", schema = "dbo")
public class RatingEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "Weight", nullable = false)
  private Double weight;
  @Column(name = "Value", nullable = false)
  private Double value;
  @Column(name = "AuthorId")
  private Integer authorId;
  @Column(name = "IdeaId")
  private Integer ideaId;
}
