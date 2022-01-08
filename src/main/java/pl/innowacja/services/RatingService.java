package pl.innowacja.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.innowacja.model.entities.RatingEntity;
import pl.innowacja.model.entities.RatingSettingEntity;
import pl.innowacja.model.enums.UserRole;
import pl.innowacja.repositories.RatingRepository;
import pl.innowacja.repositories.RatingSettingRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

  private final RatingRepository reviewRepository;
  private final RatingSettingRepository ratingSettingRepository;

  public Double getRatingForId(Integer ideaId) {
    return getNominator(ideaId) / getDenominator(ideaId);
  }

  private Double getNominator(Integer ideaId) {
    return getRatingsForIdea(ideaId).stream()
        .map(rating -> rating.getWeight() * rating.getValue())
        .reduce(0D, Double::sum);
  }

  private Double getDenominator(Integer ideaId) {
    var roleWeightMap = getRoleWeightMap(ideaId);
    return getSumOfAllWeights(roleWeightMap);
  }

  private List<RatingEntity> getRatingsForIdea(Integer ideaId) {
    return reviewRepository.findAll().stream()
        .filter(rating -> ideaId.equals(rating.getIdeaId()))
        .collect(Collectors.toUnmodifiableList());
  }

  private Double getSumOfAllWeights(Map<UserRole, Double> roleWeightMap) {
    return roleWeightMap.values().stream()
        .reduce(0D, Double::sum);
  }

  private Map<UserRole, Double> getRoleWeightMap(Integer ideaId) {
    return ratingSettingRepository.findAll().stream()
        .filter(ratingSetting -> ideaId.equals(ratingSetting.getIdeaId()))
        .collect(Collectors.toMap(
            ratingSettingEntity -> UserRole.valueOf(ratingSettingEntity.getUserRole()),
            RatingSettingEntity::getWeight,
            (duplicate, ignored) -> duplicate));
  }
}
