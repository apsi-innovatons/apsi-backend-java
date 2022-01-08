package pl.innowacja.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.innowacja.exception.IdeaServiceException;
import pl.innowacja.exception.NoResourceFoundException;
import pl.innowacja.model.dtos.*;
import pl.innowacja.model.entities.*;
import pl.innowacja.model.enums.UserRole;
import pl.innowacja.model.mapper.GenericMapper;
import pl.innowacja.model.mapper.IdeaMapper;
import pl.innowacja.model.requests.AddRatingSettingsDto;
import pl.innowacja.repositories.*;

import java.time.LocalDate;
import java.util.*;
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
  private final ReviewRepository reviewRepository;
  private final GenericMapper genericMapper;

  public List<IdeaDto> getAll() {
    var reviewSet = getCurrentUserReviewIds();

    return ideaRepository.findAll().stream()
        .map(IdeaMapper::map)
        .peek(idea -> setAlreadyReviewed(reviewSet, idea))
        .collect(Collectors.toList());
  }

  private Set<Integer> getCurrentUserReviewIds() {
    return reviewRepository.findAll().stream()
        .filter(review -> getCurrentUserId().equals(review.getAuthorId()))
        .map(ReviewEntity::getIdeaId)
        .collect(Collectors.toSet());
  }

  public Integer saveIdea(IdeaDto ideaDto) {
    ideaDto.setAuthorId(getCurrentUserId());
    ideaDto.setDate(LocalDate.now());
    var savedIdeaId = saveEntity(ideaDto).getId();
    setDefaultRatingSettingsForIdea(savedIdeaId);
    return savedIdeaId;
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

  public IdeaDto getById(Integer ideaId) {
    var ideaEntity = ideaRepository.findById(ideaId).orElseThrow(NoResourceFoundException::new);
    var ideaDto = IdeaMapper.map(ideaEntity);
    var costs = getCostsForIdea(ideaId);
    var benefits = getBenefitsForIdea(ideaId);
    if (getCurrentUserReviewIds().contains(ideaId)) {
      ideaDto.setAlreadyReviewed(Boolean.TRUE);
    }

    ideaDto.setCosts(costs);
    ideaDto.setBenefits(benefits);
    return ideaDto;
  }

  public void deleteIdea(Integer ideaId) {
    if (ideaRepository.findById(ideaId).isEmpty()) {
      throw new NoResourceFoundException();
    }
    deleteCostsByIdeaId(ideaId);
    deleteBenefitsByIdeaId(ideaId);
    deleteAttachmentsByIdeaId(ideaId);
    ideaRepository.deleteById(ideaId);
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

  public void saveRatingSettingsByIdeaId(Integer ideaId, AddRatingSettingsDto addRatingSettingsDto) {
    var ratingSettingEntities = addRatingSettingsDto.getRatingSettings().stream()
        .map(ratingSetting -> genericMapper.map(ratingSetting, RatingSettingEntity.class))
        .peek(ratingSettingEntity -> ratingSettingEntity.setIdeaId(ideaId))
        .collect(Collectors.toUnmodifiableList());

    ratingSettingRepository.saveAll(ratingSettingEntities);
  }

  public void addDecisionForIdea(DecisionDto decisionDto, Integer ideaId) {
    var ideaEntity = ideaRepository.findById(ideaId)
        .orElseThrow(() -> new NoResourceFoundException("No idea with given id exists"));
    ideaEntity.setStatus(decisionDto.getIdeaStatus().getValue());
    ideaEntity.setStatusDescription(decisionDto.getDescription());
    ideaRepository.save(ideaEntity);
  }

  private List<BenefitDto> getBenefitsForIdea(Integer ideaId) {
    return benefitRepository.findAll().stream()
        .filter(benefit -> benefit.getIdeaId().equals(ideaId))
        .map(benefitEntity -> genericMapper.map(benefitEntity, BenefitDto.class))
        .collect(Collectors.toList());
  }

  private List<CostDto> getCostsForIdea(Integer ideaId) {
    return costRepository.findAll().stream()
        .filter(cost -> cost.getIdeaId().equals(ideaId))
        .map(costEntity -> genericMapper.map(costEntity, CostDto.class))
        .collect(Collectors.toList());
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

    var costs = getCostEntities(ideaDto, savedIdea);

    log.info("Saving costs for idea {} in database.", savedIdea.getId());
    costRepository.saveAll(costs);

    var benefits = getBenefitEntities(ideaDto, savedIdea);

    log.info("Saving benefits for idea {} in database.", savedIdea.getId());
    benefitRepository.saveAll(benefits);
    return savedIdea;
  }

  private List<BenefitEntity> getBenefitEntities(IdeaDto ideaDto, IdeaEntity savedIdea) {
    return Optional.ofNullable(ideaDto)
        .map(IdeaDto::getBenefits)
        .map(benefit -> genericMapper.map(benefit, BenefitEntity.class)).stream()
        .peek(benefit -> benefit.setIdeaId(savedIdea.getId()))
        .collect(Collectors.toList());
  }

  private List<CostEntity> getCostEntities(IdeaDto ideaDto, IdeaEntity savedIdea) {
    return Optional.ofNullable(ideaDto)
        .map(IdeaDto::getCosts)
        .map(cost -> genericMapper.map(cost, CostEntity.class)).stream()
        .peek(costEntity -> costEntity.setIdeaId(savedIdea.getId()))
        .collect(Collectors.toList());
  }

  private void deleteAttachmentsByIdeaId(Integer ideaId) {
    var attachmentIds = attachmentRepository.findAll().stream()
        .filter(attachment -> attachment.getIdeaId().equals(ideaId))
        .map(AttachmentEntity::getId)
        .collect(Collectors.toList());
    attachmentRepository.deleteAllById(attachmentIds);
  }

  private void deleteBenefitsByIdeaId(Integer ideaId) {
    var benefitIds = benefitRepository.findAll().stream()
        .filter(benefit -> benefit.getIdeaId().equals(ideaId))
        .map(BenefitEntity::getId)
        .collect(Collectors.toList());
    benefitRepository.deleteAllById(benefitIds);
  }

  private void deleteCostsByIdeaId(Integer ideaId) {
    var costIds = costRepository.findAll().stream()
        .filter(cost -> cost.getIdeaId().equals(ideaId))
        .map(CostEntity::getId)
        .collect(Collectors.toList());
    costRepository.deleteAllById(costIds);
  }

  private void assertIdeaExistence(Integer ideaId) {
    if (ideaRepository.findById(ideaId).isEmpty()) {
      throw new NoResourceFoundException("Idea with given id does not exist");
    }
  }

  private void setAlreadyReviewed(Set<Integer> reviewSet, IdeaDto idea) {
    if (reviewSet.contains(idea.getId())) {
      idea.setAlreadyReviewed(true);
    }
  }

  private void setDefaultRatingSettingsForIdea(Integer ideaId) {
    var defaultRattingSettings = getDefaultRattingSettings();
    saveRatingSettingsByIdeaId(ideaId, new AddRatingSettingsDto(defaultRattingSettings));
  }

  private List<AddRatingSettingsDto.RatingSetting> getDefaultRattingSettings() {
    return Arrays.stream(UserRole.values())
        .map(userRole -> new AddRatingSettingsDto.RatingSetting(userRole, 1.0D))
        .collect(Collectors.toUnmodifiableList());
  }

  private Integer getCurrentUserId() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    return (int) authentication.getCredentials();
  }
}
