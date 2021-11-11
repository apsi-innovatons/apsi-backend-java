package pl.innowacja.model.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Costs", schema = "dbo")
public class CostEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "Title")
  private String title;
  @Column(name = "Value", nullable = false)
  private Double value;
  @Column(name = "IdeaId")
  private Integer ideaId;
}
