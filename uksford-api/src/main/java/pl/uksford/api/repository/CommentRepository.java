package pl.uksford.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uksford.api.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}