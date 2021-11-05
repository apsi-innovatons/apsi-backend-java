package pl.innowacja.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Data;

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
