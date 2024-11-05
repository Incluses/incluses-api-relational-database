package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.models.MaterialCurso;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialCursoRepository extends JpaRepository<MaterialCurso, UUID> {

    @Procedure(procedureName = "criar_material_curso")
    void criarMaterialCurso(@Param("mc_descricao") String descricao,
                            @Param("mc_curso_id") UUID cursoId,
                            @Param("mc_arquivo_id") UUID arquivoId,
                            @Param("mc_nome") String nome);
    @Procedure(procedureName = "deletar_material_curso")
    void deletarMaterialCurso(UUID mcMaterialCursoId);

    Optional<List<MaterialCurso>> findMaterialCursosByFkCursoId(UUID fkCurso);

    Optional<List<MaterialCurso>> findMaterialCursosByFkCursoIdAndNomeContainsIgnoreCase(UUID fkCurso, String nome);

}

