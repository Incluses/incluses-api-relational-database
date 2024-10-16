package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.Perfil;
import project.interdisciplinary.incluses.models.Vaga;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VagaRepository extends JpaRepository<Vaga, UUID> {

    @Procedure(procedureName = "criar_vaga")
    void criarVaga(@Param("v_descricao") String descricao,
                   @Param("v_nome") String nome,
                   @Param("v_empresa_id") UUID empresaId,
                   @Param("v_tipo_vaga_id") UUID tipoVagaId);

    Optional<List<Vaga>> findVagasByNomeContains(String nome);
    @Procedure(name = "deletar_vaga")
    void deletarVaga(UUID[] v_uuids);

    @Query("SELECT v FROM Vaga v " +
            "JOIN v.tipoVaga tv " +
            "WHERE tv.nome = :nomeTipoVaga")
    Optional<List<Vaga>> findByTipoVagaNome(@Param("nomeTipoVaga") String nomeTipoVaga);

    Optional<List<Vaga>> findVagasByFkEmpresaId(UUID fkEmpresaId);

}

