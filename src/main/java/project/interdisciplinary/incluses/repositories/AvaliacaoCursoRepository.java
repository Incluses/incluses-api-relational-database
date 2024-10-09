package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.AvaliacaoCurso;

import java.util.UUID;

public interface AvaliacaoCursoRepository extends JpaRepository<AvaliacaoCurso, UUID> {
}
