package pl.uksford.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uksford.api.entity.Instructor;

import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor, UUID> {
}