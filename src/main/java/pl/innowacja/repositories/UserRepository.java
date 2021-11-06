package pl.innowacja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.innowacja.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  default UserEntity findByUsername(String username) {
    return findAll().stream()
        .filter(user -> user.getUsername().equals(username))
        .findAny()
        .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found.", username)));
  }
}
