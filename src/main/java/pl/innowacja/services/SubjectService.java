package pl.innowacja.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.innowacja.exception.NoResourceFoundException;
import pl.innowacja.model.dtos.SubjectDto;
import pl.innowacja.model.entities.SubjectEntity;
import pl.innowacja.model.mapper.GenericMapper;
import pl.innowacja.repositories.SubjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectService {

  private final GenericMapper genericMapper;
  private final SubjectRepository subjectRepository;

  public List<SubjectDto> getAll() {
    return subjectRepository.findAll().stream()
        .map(subjectEntity -> genericMapper.map(subjectEntity, SubjectDto.class))
        .collect(Collectors.toList());
  }

  public SubjectDto getById(Integer id) {
    var subjectEntity = subjectRepository.findById(id).orElseThrow(NoResourceFoundException::new);
    return genericMapper.map(subjectEntity, SubjectDto.class);
  }

  public Integer saveSubject(SubjectDto subjectDto) {
    var subjectEntity = genericMapper.map(subjectDto, SubjectEntity.class);
    return subjectRepository.save(subjectEntity).getId();
  }
}
