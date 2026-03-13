package pl.uksford.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uksford.api.entity.ConductedClass;
import pl.uksford.api.entity.ConductedClassId;

public interface ConductedClassRepository extends JpaRepository<ConductedClass, ConductedClassId> {
}