package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.models.Perfil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CursoRepository extends JpaRepository<Curso, UUID> {

    Optional<List<Curso>> findCursosByNomeContains(String nome);

    Optional<List<Curso>> findCursosByFkPerfilId(UUID fkPerfil);

    @Procedure(name = "criar_curso")
    void criarCurso(String descricao, String nome, UUID perfilId);

    @Procedure(name = "deletar_curso")
    void deletarCurso(UUID[] c_uuids);

}