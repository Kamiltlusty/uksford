package pl.uksford.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uksford.api.entity.Course;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
}