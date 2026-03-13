package pl.uksford.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uksford.api.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}