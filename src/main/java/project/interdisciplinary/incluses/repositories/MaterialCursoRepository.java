package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.MaterialCurso;

import java.util.UUID;

public interface MaterialCursoRepository extends JpaRepository<MaterialCurso, UUID> {
}
