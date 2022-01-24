package pl.innowacja.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.innowacja.repositories.JdbcRepository;
import pl.innowacja.services.VoteService;

import java.util.Map;

@RestController
@RequestMapping("/votes")
@CrossOrigin
@Api(tags = {"backendApi"})
@RequiredArgsConstructor
public class VoteController {

  private final VoteService voteService;
  private final JdbcRepository jdbcRepository;

  @PostMapping("/{subjectId}")
  @ApiOperation(value = "Vote")
  public void voteBySubjectId(@PathVariable Integer subjectId, @RequestBody Map<Integer, Integer> votes) {
    voteService.vote(votes, subjectId);
  }

  @GetMapping("/{subjectId}")
  public Integer getNumberOfAllowedVotesForSubject(@PathVariable Integer subjectId) {
    return voteService.getNumberOfVotesAllowed(subjectId);
  }

  @GetMapping("/committee-count")
  public Integer getCommitteeUsersCount() {
    return jdbcRepository.getCommitteeUsersCount();
  }

  @PostMapping("/ideas/uncategorized/{id}")
  public void voteForUncategorizedIdea(@PathVariable Integer id, @RequestParam Boolean accept) {
    voteService.vote(id, accept);
  }
}
