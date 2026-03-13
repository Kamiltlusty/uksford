package pl.uksford.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uksford.api.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}