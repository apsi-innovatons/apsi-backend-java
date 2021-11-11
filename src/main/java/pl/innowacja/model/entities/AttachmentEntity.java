package pl.innowacja.model.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Attachments", schema = "dbo")
public class AttachmentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false)
  private Integer id;
  @Column(name = "FileName")
  private String fileName;
  @Column(name = "FlieBytes")
  private byte[] flieBytes;
  @Column(name = "IdeaId")
  private Integer ideaId;
}
