package project.interdisciplinary.incluses.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.models.Perfil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CursoRepository extends JpaRepository<Curso, UUID> {

    @Query("SELECT c FROM Curso c \n" +
            "JOIN PermissaoCurso pc ON c.id = pc.curso.id \n" +
            "WHERE pc.permissao = true \n" +
            "AND LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Optional<List<Curso>> findCursosByNomeContainsIgnoreCase(String nome);

    Optional<List<Curso>> findCursosByFkPerfilId(UUID fkPerfil);

    Optional<List<Curso>> findCursosByFkPerfilIdAndNomeContainingIgnoreCase(UUID fkPerfil, String nome);

    @Query("SELECT c FROM Curso c JOIN PermissaoCurso pc ON c.id = pc.curso.id WHERE pc.permissao = true")
    List<Curso> findAllPermissao();


    @Query(value = "SELECT criar_curso(:descricao, :nome, :perfilId)", nativeQuery = true)
    UUID criarCurso(@Param("descricao") String descricao,
                    @Param("nome") String nome,
                    @Param("perfilId") UUID perfilId);


    @Transactional
    @Modifying
    @Query(value = "CALL deletar_curso(:c_uuids)", nativeQuery = true)
    void deletarCurso(@Param("c_uuids") UUID[] c_uuids);
}