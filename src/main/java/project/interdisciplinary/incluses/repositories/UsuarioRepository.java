package project.interdisciplinary.incluses.repositories;

import jakarta.validation.constraints.Null;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import project.interdisciplinary.incluses.models.Usuario;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
//    @Procedure(name = "deletar_usuario")
//    void deleteUsuarioById(Long id);
}
