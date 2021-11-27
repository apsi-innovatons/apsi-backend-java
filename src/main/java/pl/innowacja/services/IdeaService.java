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
import pl.innowacja.model.dtos.RatingSettingDto;
import pl.innowacja.model.entities.*;
import pl.innowacja.model.mapper.GenericMapper;
import pl.innowacja.model.mapper.IdeaMapper;
import pl.innowacja.model.requests.RatingSettingCreateRequest;
import pl.innowacja.repositories.*;

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
  private final RatingSettingRepository ratingSettingRepository;
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

    assertIdeaExistence(ideaDto.getId());
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

  public void saveRatingSettingsByIdeaId(Integer ideaId, RatingSettingCreateRequest ratingSettingCreateRequest) {
    var ratingSettingEntities = ratingSettingCreateRequest.getRatingSettings().stream()
        .map(ratingSetting -> genericMapper.map(ratingSetting, RatingSettingEntity.class))
        .peek(ratingSettingEntity -> ratingSettingEntity.setIdeaId(ideaId))
        .collect(Collectors.toUnmodifiableList());

    ratingSettingRepository.saveAll(ratingSettingEntities);
  }

  public List<RatingSettingDto> getRatingSettingsByIdeaId(Integer ideaId) {
    return ratingSettingRepository.findAll().stream()
        .filter(ratingSettingEntity -> ideaId.equals(ratingSettingEntity.getIdeaId()))
        .map(ratingSettingEntity -> genericMapper.map(ratingSettingEntity, RatingSettingDto.class))
        .collect(Collectors.toUnmodifiableList());
  }

  public void updateExistingRatingSettingsByIdeaId(Integer ideaId, List<RatingSettingDto> newRatingSettings) {
    deleteRatingSettingsByIdeaId(ideaId);

    var newRatingSettingEntities = newRatingSettings.stream()
        .map(ratingSettingDto -> genericMapper.map(ratingSettingDto, RatingSettingEntity.class))
        .collect(Collectors.toUnmodifiableList());

    ratingSettingRepository.saveAll(newRatingSettingEntities);
  }

  public void deleteRatingSettingsByIdeaId(Integer ideaId) {
    assertIdeaExistence(ideaId);

    var idsToDelete = getRatingSettingsByIdeaId(ideaId).stream()
        .map(RatingSettingDto::getId)
        .collect(Collectors.toUnmodifiableList());

    ratingSettingRepository.deleteAllById(idsToDelete);
  }

  private void assertIdeaExistence(Integer ideaId) {
    if (ideaRepository.findById(ideaId).isEmpty()) {
      throw new NoResourceFoundException("Idea with given id does not exist");
    }
  }

  private Integer getCurrentUserId() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    return (int) authentication.getCredentials();
  }
}
