package project.interdisciplinary.incluses.repositories;

import jakarta.validation.constraints.Null;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.Usuario;

import javax.xml.crypto.Data;
import java.util.Date;
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
}

