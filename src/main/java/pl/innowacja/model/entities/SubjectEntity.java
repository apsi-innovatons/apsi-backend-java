package pl.innowacja.model.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Subjects", schema = "dbo")
public class SubjectEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false)
  private Integer id;
  @JsonValue
  @Column(name = "Name")
  private String name;
  @Column(name = "Done")
  private Boolean done;
  @Column(name = "Audience")
  private Integer audience;
}
