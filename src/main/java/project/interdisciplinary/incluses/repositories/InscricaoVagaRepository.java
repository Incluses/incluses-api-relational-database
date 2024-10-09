package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.InscricaoVaga;

import java.util.UUID;

public interface InscricaoVagaRepository extends JpaRepository<InscricaoVaga, UUID> {

    @Procedure(procedureName = "criar_inscricao_vaga")
    void criarInscricaoVaga(@Param("iv_usuario_id") UUID usuarioId,
                            @Param("iv_vaga_id") UUID vagaId);
}

