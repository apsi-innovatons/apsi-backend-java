package pl.innowacja.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.innowacja.exception.NoResourceFoundException;
import pl.innowacja.model.dtos.ReviewDto;
import pl.innowacja.model.entities.ReviewEntity;
import pl.innowacja.model.mapper.GenericMapper;
import pl.innowacja.model.requests.ReviewCreateRequest;
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

  public Integer saveReview(Integer ideaId, ReviewCreateRequest reviewCreateRequest) {
    var reviewEntity = new ReviewEntity();
    reviewEntity.setDate(new Date(System.currentTimeMillis()));
    reviewEntity.setDescription(reviewCreateRequest.getDescription());
    reviewEntity.setRating(reviewCreateRequest.getRating());
    reviewEntity.setAuthorId(getCurrentUserId());
    reviewEntity.setIdeaId(ideaId);

    return reviewRepository.save(reviewEntity).getId();
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

  public void updateReview(ReviewDto reviewDto) {
    if (!Objects.equals(getCurrentUserId(), reviewDto.getAuthorId())) {
      throw new SecurityException("Can not update review of another user.");
    }

    if (reviewRepository.findById(reviewDto.getId()).isEmpty()) {
      throw new NoResourceFoundException("Review with given id does not exist");
    }

    var reviewEntity = genericMapper.map(reviewDto, ReviewEntity.class);
    reviewRepository.save(reviewEntity);
  }

  private Integer getCurrentUserId() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    return (int) authentication.getCredentials();
  }
}
