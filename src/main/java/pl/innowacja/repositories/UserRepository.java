package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.innowacja.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
