package pl.innowacja.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.innowacja.model.dtos.IdeaAttachmentDto;
import pl.innowacja.services.SecurityContextUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcRepository {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

  public Integer getUserRole(Integer userId) {
    var sqlQuery = "select userrole from Users where id = :id";
    var paramMap = new MapSqlParameterSource("id", userId);
    return namedParameterJdbcTemplate.queryForObject(sqlQuery, paramMap, Integer.class);
  }

  public Boolean alreadyVotedInGivenSubject(Integer subjectId) {
    var sqlQuery = "select count(0) from Vote v join ideas i on v.IdeaId = i.Id where v.CommitteeMemberId = :userId " +
        "and i.SubjectId = :subjectId";
    var paramMap = new MapSqlParameterSource();
    paramMap.addValue("userId", SecurityContextUtil.getCurrentUserId());
    paramMap.addValue("subjectId", subjectId);

    var count = namedParameterJdbcTemplate.queryForObject(sqlQuery, paramMap, Integer.class);

    return count == null ? false : count > 0;
  }
}
