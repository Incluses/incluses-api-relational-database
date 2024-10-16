package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.InscricaoCurso;
import project.interdisciplinary.incluses.models.InscricaoVaga;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InscricaoVagaRepository extends JpaRepository<InscricaoVaga, UUID> {

    @Procedure(procedureName = "criar_inscricao_vaga")
    void criarInscricaoVaga(@Param("iv_usuario_id") UUID usuarioId,
                            @Param("iv_vaga_id") UUID vagaId);

    @Query("SELECT iv FROM InscricaoVaga iv WHERE iv.fkVagaId = ?1 and iv.fkUsuarioId = ?2")
    Optional<InscricaoVaga> findInscricaoExistente(UUID fkVaga, UUID fkUsuario);

    Optional<List<InscricaoVaga>> findInscricaoVagasByFkUsuarioId(UUID fkUsuario);

}

