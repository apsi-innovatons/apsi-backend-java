package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.innowacja.model.entities.RatingSettingEntity;

public interface RatingSettingRepository extends JpaRepository<RatingSettingEntity, Integer> {
}
