package pl.innowacja.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.innowacja.exception.IdeaServiceException;
import pl.innowacja.exception.NoResourceFoundException;
import pl.innowacja.model.dtos.ReviewDto;
import pl.innowacja.model.entities.IdeaEntity;
import pl.innowacja.model.entities.RatingSettingEntity;
import pl.innowacja.model.entities.ReviewEntity;
import pl.innowacja.model.entities.UserEntity;
import pl.innowacja.model.enums.UserRole;
import pl.innowacja.model.mapper.GenericMapper;
import pl.innowacja.model.requests.AddReviewDto;
import pl.innowacja.repositories.IdeaRepository;
import pl.innowacja.repositories.JdbcRepository;
import pl.innowacja.repositories.RatingSettingRepository;
import pl.innowacja.repositories.ReviewRepository;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final GenericMapper genericMapper;
  private final RatingSettingRepository ratingSettingRepository;
  private final IdeaRepository ideaRepository;
  private final JdbcRepository jdbcRepository;

  public Integer saveReview(Integer ideaId, AddReviewDto addReviewDto) {
    if (reviewOfCurrentUserAlreadyExists(ideaId)) { // TODO this should be handled by composite primary keys
      throw new IdeaServiceException("Review for given idea by current user already exists", HttpStatus.BAD_REQUEST);
    }
    var reviewEntity = new ReviewEntity();
    reviewEntity.setDate(new Date(System.currentTimeMillis()));
    reviewEntity.setDescription(addReviewDto.getDescription());
    reviewEntity.setRating(addReviewDto.getRating());
    reviewEntity.setWeight(getRatingWeightForCurrentUser(ideaId));
    reviewEntity.setAuthorId(getCurrentUserId());
    reviewEntity.setIdeaId(ideaId);

    var reviewId =  reviewRepository.save(reviewEntity).getId();
    updateIdeaRating(ideaId);
    return reviewId;
  }

  private boolean reviewOfCurrentUserAlreadyExists(Integer ideaId) {
    return reviewRepository.findAll().stream()
        .filter(review -> ideaId.equals(review.getIdeaId()))
        .anyMatch(review -> getCurrentUserId().equals(review.getAuthorId()));
  }

  public List<ReviewDto> getReviewsByIdeaId(Integer ideaId) {
    return reviewRepository.findAll().stream()
        .filter(review -> ideaId.equals(review.getIdeaId()))
        .map(reviewEntity -> genericMapper.map(reviewEntity, ReviewDto.class))
        .collect(Collectors.toList());
  }

  public List<ReviewDto> getReviewsByUserId(Integer userId) {
    return reviewRepository.findAll().stream()
        .filter(review -> userId.equals(review.getAuthorId()))
        .map(reviewEntity -> genericMapper.map(reviewEntity, ReviewDto.class))
        .collect(Collectors.toList());
  }

  public List<ReviewDto> getReviewsOfCurrentUser() {
    return getReviewsByUserId(getCurrentUserId());
  }

  public void updateExistingRatingWeights(List<RatingSettingEntity> ratingSettingEntities) {
    ratingSettingEntities
        .forEach(this::updateAllReviewsWithGivenRoleForId);
  }

  private void updateAllReviewsWithGivenRoleForId(RatingSettingEntity ratingSettingEntity) {
    var ideaId = ratingSettingEntity.getIdeaId();
    reviewRepository.findAll().stream()
        .filter(review -> ideaId.equals(review.getIdeaId()))
        .filter(review -> jdbcRepository.getUserRole(review.getAuthorId()).equals(ratingSettingEntity.getUserRole()))
        .forEach(review -> {
          review.setWeight(ratingSettingEntity.getWeight());
          reviewRepository.save(review);
        });
  }

  public void updateReview(ReviewDto reviewDto) {
    if (!Objects.equals(getCurrentUserId(), reviewDto.getAuthorId()) && getCurrentUserRole() != UserRole.Admin) {
      throw new SecurityException("Can not update review of another user.");
    }

    if (reviewRepository.findById(reviewDto.getId()).isEmpty()) {
      throw new NoResourceFoundException("Review with given id does not exist");
    }

    var reviewEntity = genericMapper.map(reviewDto, ReviewEntity.class);
    reviewRepository.save(reviewEntity);
  }

  private Double getRatingWeightForCurrentUser(Integer ideaId) {
    var currentUserRole = getCurrentUserRole();
    return getRatingSettingsByIdeaId(ideaId).stream()
        .filter(setting -> currentUserRole.getValue().equals(setting.getUserRole()))
        .findAny()
        .map(RatingSettingEntity::getWeight)
        .orElseThrow(() -> new NoResourceFoundException("No rating setting for given user role."));
  }

  public Double getIdeaRating(Integer ideaId) {
    var nominator = getNominator(ideaId);
    var denominator = getDenominator(ideaId);
    if (denominator != 0) {
      return nominator / denominator;
    }
    return 0D;
  }

  private Double getNominator(Integer ideaId) {
    return getRatingsForIdea(ideaId).stream()
        .map(review -> review.getWeight() * review.getRating())
        .reduce(0D, Double::sum);
  }

  private Double getDenominator(Integer ideaId) {
    return getSumOfAllWeights(ideaId);
  }

  private List<ReviewEntity> getRatingsForIdea(Integer ideaId) {
    return reviewRepository.findAll().stream()
        .filter(review -> ideaId.equals(review.getIdeaId()))
        .collect(Collectors.toUnmodifiableList());
  }

  private Double getSumOfAllWeights(Integer ideaId) {
    return reviewRepository.findAll().stream()
        .filter(review -> ideaId.equals(review.getIdeaId()))
        .map(ReviewEntity::getWeight)
        .reduce(0D, Double::sum);
  }

  private List<RatingSettingEntity> getRatingSettingsByIdeaId(Integer ideaId) {
    return ratingSettingRepository.findAll().stream()
        .filter(ratingSetting -> ideaId.equals(ratingSetting.getIdeaId()))
        .collect(Collectors.toUnmodifiableList());
  }

  private UserRole getCurrentUserRole() {
    var userEntity = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return UserRole.valueOf(userEntity.getUserRole());
  }

  public void updateIdeaRating(Integer ideaId) {
    var ideaRating = getIdeaRating(ideaId);
    var ideaEntity = ideaRepository.findById(ideaId)
        .orElseThrow(() -> new NoResourceFoundException("No idea with given id exists"));
    ideaEntity.setRating(ideaRating);
    ideaRepository.save(ideaEntity);
  }

  private Integer getCurrentUserId() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    return (int) authentication.getCredentials();
  }
}
