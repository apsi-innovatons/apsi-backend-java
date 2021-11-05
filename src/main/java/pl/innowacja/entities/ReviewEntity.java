package pl.innowacja.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "Reviews", schema = "dbo")
public class ReviewEntity {

  @Id
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "Description")
  private String description;
  @Column(name = "Rating", nullable = false)
  private Integer rating;
  @Column(name = "Date", nullable = false)
  private Date date;
  @Column(name = "IdeaId")
  private Integer ideaId;
  @Column(name = "AuthorId")
  private Integer authorId;
}
