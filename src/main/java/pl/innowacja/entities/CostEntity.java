package pl.innowacja.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "Costs", schema = "dbo")
public class CostEntity {

  @Id
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "Title")
  private String title;
  @Column(name = "Value", nullable = false)
  private Double value;
  @Column(name = "IdeaId")
  private Integer ideaId;
}
