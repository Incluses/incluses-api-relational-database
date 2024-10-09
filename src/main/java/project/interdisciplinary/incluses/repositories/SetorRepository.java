package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.Setor;

import java.util.UUID;

public interface SetorRepository extends JpaRepository<Setor, UUID> {

    @Procedure(procedureName = "deletar_setor")
    void deletarSetor(@Param("s_setor_id") UUID setorId);
    @Procedure(procedureName = "criar_setor")
    void criarSetor(String s_nome);

}
