package pl.innowacja.model.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Benefits", schema = "dbo")
public class BenefitEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "Title")
  private String title;
  @Column(name = "Description")
  private String description;
  @Column(name = "IdeaId")
  private Integer ideaId;
}
