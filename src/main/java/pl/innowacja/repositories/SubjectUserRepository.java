package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.innowacja.model.entities.SubjectUserEntity;
import pl.innowacja.model.entities.SubjectUserEntityPK;

public interface SubjectUserRepository extends JpaRepository<SubjectUserEntity, SubjectUserEntityPK> {
}
