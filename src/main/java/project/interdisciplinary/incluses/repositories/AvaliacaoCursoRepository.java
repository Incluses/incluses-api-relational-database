package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.interdisciplinary.incluses.models.AvaliacaoCurso;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvaliacaoCursoRepository extends JpaRepository<AvaliacaoCurso, UUID> {

    @Query("SELECT a FROM AvaliacaoCurso a WHERE a.fkUsuarioId = ?1")
    Optional<List<AvaliacaoCurso>> findAvaliacaoCursosByFkUsuario(UUID fkUsuario);

    @Query("SELECT a FROM AvaliacaoCurso a WHERE a.fkCursoId = ?1")
    Optional<List<AvaliacaoCurso>> findAvaliacaoCursosByFkCurso(UUID fkCurso);
}
