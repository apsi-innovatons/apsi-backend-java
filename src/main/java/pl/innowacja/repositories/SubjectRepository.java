package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.innowacja.entities.SubjectEntity;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Integer> {
}
