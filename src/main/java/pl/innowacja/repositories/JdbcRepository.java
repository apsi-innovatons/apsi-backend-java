package pl.innowacja.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.innowacja.model.dtos.IdeaAttachmentDto;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  public Integer getCommitteeUsersCount() {
    var sqlQuery = "select count(0) from users where userrole = 2";
    return jdbcTemplate.queryForObject(sqlQuery, Integer.class);
  }

  public List<Integer> getAllIdeaIds() {
    var sqlQuery = "select id from ideas";
    return jdbcTemplate.queryForList(sqlQuery, Integer.class);
  }

  public List<IdeaAttachmentDto> getAttachmentIdeaMap() {
    var sqlQuery = "select id, ideaId from Attachments";
    return jdbcTemplate.query(sqlQuery, BeanPropertyRowMapper.newInstance(IdeaAttachmentDto.class));
  }
}
