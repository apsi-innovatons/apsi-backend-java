package pl.innowacja.model.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "PostAnswers", schema = "dbo", catalog = "apsi-database")
public class PostAnswersEntity {
  private Integer id;
  private String text;
  private Date date;

  @Id
  @Column(name = "Id", nullable = false)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Basic
  @Column(name = "Text", nullable = true, length = 2147483647)
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Basic
  @Column(name = "Date", nullable = false)
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PostAnswersEntity that = (PostAnswersEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(text, that.text) && Objects.equals(date, that.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text, date);
  }
}
