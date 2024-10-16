package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.InscricaoCurso;
import project.interdisciplinary.incluses.models.InscricaoVaga;
import project.interdisciplinary.incluses.models.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface InscricaoCursoRepository extends JpaRepository<InscricaoCurso, UUID> {

    @Procedure(procedureName = "criar_inscricao_curso")
    void criarInscricaoCurso(@Param("ic_usuario_id") UUID usuarioId,
                             @Param("ic_curso_id") UUID cursoId);

    @Query("SELECT ic FROM InscricaoCurso ic WHERE ic.fkCursoId = ?1 and ic.fkUsuarioId = ?2")
    Optional<InscricaoCurso> findInscricaoExistente(UUID fkCurso, UUID fkUsuario);

    Optional<List<InscricaoCurso>> findInscricaoCursosByFkUsuarioId(UUID fkUsuario);

}

