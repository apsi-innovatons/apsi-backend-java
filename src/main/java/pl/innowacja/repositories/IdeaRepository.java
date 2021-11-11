package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.innowacja.model.entities.IdeaEntity;

public interface IdeaRepository extends JpaRepository<IdeaEntity, Integer> {
}
