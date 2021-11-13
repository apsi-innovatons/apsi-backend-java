package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.innowacja.model.entities.AttachmentEntity;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Integer> {
}
