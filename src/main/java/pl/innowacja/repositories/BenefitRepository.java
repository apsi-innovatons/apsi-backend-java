package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.innowacja.entities.BenefitEntity;

public interface BenefitRepository extends JpaRepository<BenefitEntity, Integer> {
}
