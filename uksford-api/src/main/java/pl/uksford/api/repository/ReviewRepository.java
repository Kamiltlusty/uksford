package pl.uksford.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uksford.api.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}