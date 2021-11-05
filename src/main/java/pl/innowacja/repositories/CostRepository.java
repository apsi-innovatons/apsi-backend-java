package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.innowacja.entities.CostEntity;

public interface CostRepository extends JpaRepository<CostEntity, Integer> {
}
