package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.innowacja.model.entities.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
}
