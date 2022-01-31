package pl.innowacja.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.innowacja.exception.NoResourceFoundException;
import pl.innowacja.model.dtos.CostDto;
import pl.innowacja.model.dtos.SubjectDto;
import pl.innowacja.model.entities.CostEntity;
import pl.innowacja.model.entities.SubjectEntity;
import pl.innowacja.model.entities.SubjectUserEntity;
import pl.innowacja.model.entities.SubjectUserEntityPK;
import pl.innowacja.model.mapper.GenericMapper;
import pl.innowacja.repositories.SubjectRepository;
import pl.innowacja.repositories.SubjectUserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectService {

  private final GenericMapper genericMapper;
  private final SubjectRepository subjectRepository;
  private final SubjectUserRepository subjectUserRepository;

  public List<SubjectDto> getAll() {
    var subjectMap = subjectUserRepository.findAll().stream()
        .collect(Collectors.groupingBy(
            SubjectUserEntity::getSubjectsId,
            Collectors.mapping(SubjectUserEntity::getCommitteeMembersId, Collectors.toUnmodifiableList())));

    return subjectRepository.findAll().stream()
        .map(subjectEntity -> genericMapper.map(subjectEntity, SubjectDto.class))
        .peek(subjectDto -> setCommitteeMembers(subjectMap, subjectDto))
        .collect(Collectors.toList());
  }

  public SubjectDto getById(Integer id) {
    var subjectEntity = subjectRepository.findById(id).orElseThrow(NoResourceFoundException::new);
    return genericMapper.map(subjectEntity, SubjectDto.class);
  }

  public Integer saveSubject(SubjectDto subjectDto) {
    var subjectEntity = genericMapper.map(subjectDto, SubjectEntity.class);
    var subjectId = subjectRepository.save(subjectEntity).getId();

    if (subjectDto.getCommitteeMembers() != null) {
      subjectDto.getCommitteeMembers()
          .forEach(member -> subjectUserRepository.save(new SubjectUserEntity(member, subjectId)));
    }

    return subjectId;
  }

  public List<Integer> getCommitteeMembersForSubject(Integer subjectId) {
    return subjectUserRepository.findAll().stream()
        .filter(subjectUserEntity -> subjectUserEntity.getSubjectsId().equals(subjectId))
        .map(SubjectUserEntity::getCommitteeMembersId)
        .collect(Collectors.toUnmodifiableList());
  }

  public List<SubjectDto> getSubjectIdsOfCurrentUser() {
    var currentUserId = getCurrentUserId();
    var subjectIds = subjectUserRepository.findAll().stream()
        .filter(subjectUserEntity -> subjectUserEntity.getCommitteeMembersId().equals(currentUserId))
        .map(SubjectUserEntity::getSubjectsId)
        .collect(Collectors.toUnmodifiableList());

    return subjectRepository.findAllById(subjectIds).stream()
        .map(subjectEntity -> genericMapper.map(subjectEntity, SubjectDto.class))
        .collect(Collectors.toUnmodifiableList());
  }

  private void setCommitteeMembers(Map<Integer, List<Integer>> subjectMap, SubjectDto subjectDto) {
    if (subjectMap.containsKey(subjectDto.getId())) {
      subjectDto.setCommitteeMembers(subjectMap.get(subjectDto.getId()));
    }
  }

  private Integer getCurrentUserId() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    return (int) authentication.getCredentials();
  }
}
