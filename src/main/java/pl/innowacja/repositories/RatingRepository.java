package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.innowacja.model.entities.RatingEntity;

public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
}
