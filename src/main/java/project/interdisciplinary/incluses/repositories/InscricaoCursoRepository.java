package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.InscricaoCurso;

import java.util.UUID;
public interface InscricaoCursoRepository extends JpaRepository<InscricaoCurso, UUID> {

    @Procedure(procedureName = "criar_inscricao_curso")
    void criarInscricaoCurso(@Param("ic_usuario_id") UUID usuarioId,
                             @Param("ic_curso_id") UUID cursoId);
}

