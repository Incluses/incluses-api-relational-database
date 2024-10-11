package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.Usuario;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    @Procedure(procedureName = "deletar_usuario")
    void deletarUsuario(@Param("id_usuario") UUID idUsuario);

    @Procedure(procedureName = "criar_usuario")
    void criarUsuario(@Param("u_cpf") String cpf,
                      @Param("u_dt_nascimento") Date dtNascimento,
                      @Param("u_pronomes") String pronomes,
                      @Param("u_nome_social") String nomeSocial,
                      @Param("p_nome") String nome,
                      @Param("p_senha") String senha,
                      @Param("p_email") String email,
                      @Param("t_telefone") String telefone);

    @Query("SELECT u FROM Usuario u WHERE u.fkPerfilId = ?1")
    Optional<Usuario> findUserByFkPerfil(UUID fkPerfil);
}

