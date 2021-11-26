package pl.innowacja.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RatingSettings", schema = "dbo")
public class RatingSettingEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "UserRole")
  private Integer userRole;
  @Column(name = "Weight")
  private Double weight;
  @Column(name = "IdeaId")
  private Integer ideaId;
}
