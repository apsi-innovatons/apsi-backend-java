package pl.innowacja.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "Ideas", schema = "dbo")
public class IdeaEntity {

  @Id
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "Title")
  private String title;
  @Column(name = "Description")
  private String description;
  @Column(name = "Status", nullable = false)
  private Integer status;
  @Column(name = "StatusDescription")
  private String statusDescription;
  @Column(name = "Date", nullable = false)
  private Date date;
  @Column(name = "Blocked", nullable = false)
  private Boolean blocked;
  @Column(name = "Anonymous", nullable = false)
  private Boolean anonymous;
  @Column(name = "AuthorId")
  private Integer authorId;
  @ManyToOne
  @JoinColumn(name = "SubjectId", referencedColumnName = "Id")
  private SubjectEntity subject;
}