package pl.innowacja.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "Subjects", schema = "dbo")
public class SubjectEntity {

  @Id
  @Column(name = "Id", nullable = false)
  private Integer id;
  @JsonValue
  @Column(name = "Name")
  private String name;
  @Column(name = "Audience")
  private Integer audience;
}
