package pl.innowacja.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.innowacja.exception.IdeaServiceException;
import pl.innowacja.exception.NoResourceFoundException;
import pl.innowacja.model.dtos.BenefitDto;
import pl.innowacja.model.dtos.CostDto;
import pl.innowacja.model.dtos.IdeaDto;
import pl.innowacja.model.entities.AttachmentEntity;
import pl.innowacja.model.entities.BenefitEntity;
import pl.innowacja.model.entities.CostEntity;
import pl.innowacja.model.entities.IdeaEntity;
import pl.innowacja.model.mapper.GenericMapper;
import pl.innowacja.model.mapper.IdeaMapper;
import pl.innowacja.repositories.AttachmentRepository;
import pl.innowacja.repositories.BenefitRepository;
import pl.innowacja.repositories.CostRepository;
import pl.innowacja.repositories.IdeaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdeaService {

  private final IdeaRepository ideaRepository;
  private final BenefitRepository benefitRepository;
  private final CostRepository costRepository;
  private final AttachmentRepository attachmentRepository;
  private final GenericMapper genericMapper;

  public List<IdeaDto> getAll() {
    return ideaRepository.findAll().stream()
        .map(IdeaMapper::map)
        .collect(Collectors.toList());
  }

  public Integer saveIdea(IdeaDto ideaDto) {
    ideaDto.setAuthorId(getCurrentUserId());
    ideaDto.setDate(LocalDate.now());
    return saveEntity(ideaDto).getId();
  }

  public void update(IdeaDto ideaDto) {
    validateUpdateDto(ideaDto);
    saveEntity(ideaDto);
  }

  public List<IdeaDto> getIdeasForSubject(Integer subjectId) {
    return ideaRepository.findAll().stream()
        .filter(idea -> idea.getSubjectId().equals(subjectId))
        .map(IdeaMapper::map)
        .collect(Collectors.toList());
  }

  public IdeaDto getById(Integer id) {
    var ideaEntity = ideaRepository.findById(id).orElseThrow(NoResourceFoundException::new);
    var ideaDto = IdeaMapper.map(ideaEntity);
    var costs = costRepository.findAll().stream()
        .filter(cost -> cost.getIdeaId().equals(id))
        .map(costEntity -> genericMapper.map(costEntity, CostDto.class))
        .collect(Collectors.toList());

    var benefits = benefitRepository.findAll().stream()
        .filter(benefit -> benefit.getIdeaId().equals(id))
        .map(benefitEntity -> genericMapper.map(benefitEntity, BenefitDto.class))
        .collect(Collectors.toList());

    ideaDto.setCosts(costs);
    ideaDto.setBenefits(benefits);
    return ideaDto;
  }

  private void validateUpdateDto(IdeaDto ideaDto) {
    if (!Objects.equals(ideaDto.getAuthorId(), getCurrentUserId())) {
      throw new AuthorizationServiceException("AuthorId does not match userId from token.");
    }

    if (ideaDto.getId() == null) {
      throw new IdeaServiceException("Id can not be null.", HttpStatus.BAD_REQUEST);
    }

    if (ideaRepository.findById(ideaDto.getId()).isEmpty()) {
      throw new NoResourceFoundException("Idea with given id does not exist.");
    }
  }

  private IdeaEntity saveEntity(IdeaDto ideaDto) {
    var ideaEntity = IdeaMapper.map(ideaDto);
    log.info("Saving idea in database.");
    var savedIdea = ideaRepository.save(ideaEntity);

    var costs = ideaDto.getCosts().stream()
        .map(cost -> genericMapper.map(cost, CostEntity.class))
        .peek(costEntity -> costEntity.setIdeaId(savedIdea.getId()))
        .collect(Collectors.toList());

    log.info("Saving costs for idea {} in database.", savedIdea.getId());
    costRepository.saveAll(costs);

    var benefits = ideaDto.getBenefits().stream()
        .map(benefit -> genericMapper.map(benefit, BenefitEntity.class))
        .peek(benefit -> benefit.setIdeaId(savedIdea.getId()))
        .collect(Collectors.toList());

    log.info("Saving benefits for idea {} in database.", savedIdea.getId());
    benefitRepository.saveAll(benefits);
    return savedIdea;
  }

  public void deleteIdea(Integer id) {
    if (ideaRepository.findById(id).isEmpty()) {
      throw new NoResourceFoundException();
    }
    var costIds = costRepository.findAll().stream()
        .filter(cost -> cost.getIdeaId().equals(id))
        .map(CostEntity::getId)
        .collect(Collectors.toList());
    costRepository.deleteAllById(costIds);

    var benefitIds = benefitRepository.findAll().stream()
        .filter(benefit -> benefit.getIdeaId().equals(id))
        .map(BenefitEntity::getId)
        .collect(Collectors.toList());
    benefitRepository.deleteAllById(benefitIds);

    var attachmentIds = attachmentRepository.findAll().stream()
        .filter(attachment -> attachment.getIdeaId().equals(id))
        .map(AttachmentEntity::getId)
        .collect(Collectors.toList());
    attachmentRepository.deleteAllById(attachmentIds);
    ideaRepository.deleteById(id);
  }

  private Integer getCurrentUserId() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    return (int) authentication.getCredentials();
  }
}
