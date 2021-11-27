package pl.innowacja.controllers;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.innowacja.model.dtos.ReviewDto;
import pl.innowacja.model.requests.ReviewCreateRequest;
import pl.innowacja.services.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@Api(tags = {"backendApi"})
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping("/ideas/{ideaId}/reviews")
  public Integer saveReviewByIdeaId(@PathVariable Integer ideaId, @RequestBody @Valid ReviewCreateRequest reviewCreateRequest) {
    return reviewService.saveReview(ideaId, reviewCreateRequest);
  }

  @GetMapping("/ideas/{ideaId}/reviews")
  public List<ReviewDto> getReviewsByIdeaId(@PathVariable Integer ideaId) {
    return reviewService.getReviewsByIdeaId(ideaId);
  }

  @GetMapping("/users/{userId}/reviews")
  public List<ReviewDto> getReviewsByUserId(@PathVariable Integer userId) {
    return reviewService.getReviewsByIdeaId(userId);
  }

  @GetMapping("/reviews")
  public List<ReviewDto> getReviewsOfCurrentUser() {
    return reviewService.getReviewsOfCurrentUser();
  }

  @PutMapping("/reviews")
  public void updateExistingReview(@RequestBody ReviewDto reviewDto) {
    reviewService.updateReview(reviewDto);
  }
}
