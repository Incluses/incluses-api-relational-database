package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.InscricaoCurso;

import java.util.UUID;

public interface InscricaoCursoRepository extends JpaRepository<InscricaoCurso, UUID> {
}
