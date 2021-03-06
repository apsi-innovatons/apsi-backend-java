package pl.innowacja.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.innowacja.exception.IdeaServiceException;
import pl.innowacja.model.entities.IdeaEntity;
import pl.innowacja.model.entities.SubjectUserEntity;
import pl.innowacja.model.entities.VoteEntity;
import pl.innowacja.repositories.IdeaRepository;
import pl.innowacja.repositories.JdbcRepository;
import pl.innowacja.repositories.SubjectUserRepository;
import pl.innowacja.repositories.VoteRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class VoteService {
  private final VoteRepository voteRepository;
  private final IdeaRepository ideaRepository;
  private final SubjectUserRepository subjectUserRepository;
  private final JdbcRepository jdbcRepository;

  public void vote(Map<Integer, Integer> votes, Integer subjectId) {
    var currentUserId = SecurityContextUtil.getCurrentUserId();
    validateVoting(votes, subjectId);
    votes.forEach((ideaId, value) -> saveVote(ideaId, value, currentUserId));
  }

  public void vote(Integer ideaId, Boolean accept) {
    var currentUserId = SecurityContextUtil.getCurrentUserId();

    var ideaEntity = getIdea(ideaId); // to assert existence

    if (ideaEntity.getSubjectId() != null) {
      throw new IdeaServiceException("This idea belongs to a subject, decision vorting is forbidden", HttpStatus.BAD_REQUEST);
    }

    if (voteRepository.findAll().stream()
        .anyMatch(vote -> currentUserId.equals(vote.getCommitteeMemberId()) && ideaId.equals(vote.getIdeaId()))) {
      throw new IdeaServiceException("This user has already voted for this idea.", HttpStatus.BAD_REQUEST);
    }

    saveBinaryVote(ideaId, accept, currentUserId);
    updateIdea(accept, ideaEntity);
  }

  public Integer getNumberOfVotesAllowed(Integer subjectId) {
    var numberOfIdeas = Math.toIntExact(ideaRepository.findAll().stream()
        .filter(idea -> subjectId.equals(idea.getSubjectId()))
        .count());

    return Math.min(numberOfIdeas, 5);
  }

  private void saveVote(Integer ideaId, Integer value, Integer currentUserId) {
    var voteEntity = new VoteEntity();
    voteEntity.setIdeaId(ideaId);
    voteEntity.setValue(value);
    voteEntity.setCommitteeMemberId(currentUserId);
    voteRepository.save(voteEntity);

    updateIdea(ideaId, value);
  }

  private void updateIdea(Boolean accept, IdeaEntity ideaEntity) {
    if (accept) {
      ideaEntity.setVotesSum(ideaEntity.getVotesSum() + 1);
    } else {
      ideaEntity.setRejectsSum(ideaEntity.getRejectsSum() + 1);
    }
    ideaRepository.save(ideaEntity);
  }

  private void saveBinaryVote(Integer ideaId, Boolean accept, Integer currentUserId) {
    var voteEntity = new VoteEntity();
    voteEntity.setCommitteeMemberId(currentUserId);
    voteEntity.setIdeaId(ideaId);
    voteEntity.setValue(accept ? 1 : 0);
    voteRepository.save(voteEntity);
  }

  private void updateIdea(Integer ideaId, Integer value) {
    var ideaEntityOptional = ideaRepository.findById(ideaId);
    if (ideaEntityOptional.isPresent()) {
      var ideaEntity = ideaEntityOptional.get();
      ideaEntity.setVotesSum(ideaEntity.getVotesSum() + value);
      ideaRepository.save(ideaEntity);
    }
  }

  private void validateVoting(Map<Integer, Integer> votes, Integer subjectId) {
    var ideaIdsSet = new HashSet<>(jdbcRepository.getAllIdeaIds());
    validateVoteMap(votes, subjectId);
    var currentUserId = SecurityContextUtil.getCurrentUserId();
    var currentUserVotes = voteRepository.findAll().stream()
        .filter(vote -> currentUserId.equals(vote.getCommitteeMemberId()))
        .collect(Collectors.toMap(VoteEntity::getIdeaId, Function.identity()));

    votes.forEach((ideaId, value) -> assertNotVotedYet(currentUserVotes, ideaId));
    votes.forEach((ideaId, value) -> assertIdeaExists(ideaIdsSet, ideaId));
    userEligibleToVote(subjectId);
  }

  private void assertIdeaExists(HashSet<Integer> ideaIdsSet, Integer ideaId) {
    if (!ideaIdsSet.contains(ideaId)) {
      throw new IdeaServiceException(String.format("Idea with id %d does not exist", ideaId), HttpStatus.BAD_REQUEST);
    }
  }

  private IdeaEntity getIdea(Integer ideaId) {
    return ideaRepository.findById(ideaId)
        .orElseThrow(
            () -> new IdeaServiceException(String.format("Idea with id: %d does not exist", ideaId), HttpStatus.BAD_REQUEST));
  }

  private void userEligibleToVote(Integer subjectId) {
    if (subjectUserRepository.findAll().stream()
        .noneMatch(subjectUserEntity -> subjectAndUserMatch(subjectId, subjectUserEntity))) {
      throw new IdeaServiceException(String.format("Current user does not belong to the committee of subject %d", subjectId), HttpStatus.BAD_REQUEST);
    }
  }

  private boolean subjectAndUserMatch(Integer subjectId, SubjectUserEntity subjectUserEntity) {
    return subjectUserEntity.getSubjectsId().equals(subjectId) && subjectUserEntity.getCommitteeMembersId().equals(SecurityContextUtil.getCurrentUserId());
  }

  private void assertNotVotedYet(Map<Integer, VoteEntity> currentUserVotes, Integer key) {
    if (currentUserVotes.containsKey(key)) {
      throw new IdeaServiceException(String.format("Current user has already voted for ideaId %d", key), HttpStatus.BAD_REQUEST);
    }
  }

  private void validateVoteMap(Map<Integer, Integer> votes, Integer subjectId) {

    var numberOfVotesAllowed = getNumberOfVotesAllowed(subjectId);
    var votingAllowedValues = IntStream.rangeClosed(1, numberOfVotesAllowed)
        .boxed().collect(Collectors.toSet());

    votingAllowedValues
        .forEach(value -> assertValueIsPresent(votes, value));
    assertMapSize(votes, numberOfVotesAllowed);
  }

  private void assertMapSize(Map<Integer, Integer> votes, int size) {
    if (votes.size() != size) {
      invalidMap();
    }
  }

  private void assertValueIsPresent(Map<Integer, Integer> votes, Integer value) {
    if (!votes.containsValue(value)) {
      invalidMap();
    }
  }

  private void invalidMap() {
    throw new IdeaServiceException("Invalid vote map, it should contain values from 1 to 5", HttpStatus.BAD_REQUEST);
  }
}
