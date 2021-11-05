package pl.innowacja.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Attachments", schema = "dbo")
public class AttachmentEntity {

  @Id
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "FileName")
  private String fileName;
  @Column(name = "FlieBytes")
  private byte[] flieBytes;
  @Column(name = "IdeaId")
  private Integer ideaId;
}