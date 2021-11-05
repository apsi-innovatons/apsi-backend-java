package pl.innowacja.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "Benefits", schema = "dbo")
public class BenefitEntity {

  @Id
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "Title")
  private String title;
  @Column(name = "Description")
  private String description;
  @Column(name = "IdeaId")
  private Integer ideaId;
}