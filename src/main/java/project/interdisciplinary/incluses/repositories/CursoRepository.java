package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.Curso;

import java.util.UUID;

public interface CursoRepository extends JpaRepository<Curso, UUID> {
}