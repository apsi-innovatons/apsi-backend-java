package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.innowacja.model.entities.VoteEntity;

public interface VoteRepository extends JpaRepository<VoteEntity, Integer> {
}
