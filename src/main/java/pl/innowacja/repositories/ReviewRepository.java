package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.innowacja.entities.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
}
