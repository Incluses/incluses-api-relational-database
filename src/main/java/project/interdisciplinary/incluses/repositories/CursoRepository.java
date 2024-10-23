package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.models.Perfil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CursoRepository extends JpaRepository<Curso, UUID> {

    Optional<List<Curso>> findCursosByNomeContainsIgnoreCase(String nome);

    Optional<List<Curso>> findCursosByFkPerfilId(UUID fkPerfil);

    @Query("SELECT c FROM Curso c JOIN PermissaoCurso pc ON c.id = pc.curso.id WHERE pc.permissao = true")
    List<Curso> findAllPermissao();


    @Query(value = "SELECT criar_curso(:descricao, :nome, :perfilId)", nativeQuery = true)
    UUID criarCurso(@Param("descricao") String descricao,
                    @Param("nome") String nome,
                    @Param("perfilId") UUID perfilId);


    @Procedure(name = "deletar_curso")
    void deletarCurso(UUID[] c_uuids);

}