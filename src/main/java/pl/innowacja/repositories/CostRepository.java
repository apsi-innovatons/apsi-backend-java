package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.innowacja.model.entities.CostEntity;

public interface CostRepository extends JpaRepository<CostEntity, Integer> {
}
