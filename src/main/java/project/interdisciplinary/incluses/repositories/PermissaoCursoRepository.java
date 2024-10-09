package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.PermissaoCurso;

import java.util.UUID;

public interface PermissaoCursoRepository extends JpaRepository<PermissaoCurso, UUID> {
}
