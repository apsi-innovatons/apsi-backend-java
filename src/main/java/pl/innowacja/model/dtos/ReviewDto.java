package pl.innowacja.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
  private Integer id;
  private String description;
  private Integer rating;
  private Date date;
  private Integer ideaId;
  private Integer authorId;
}
