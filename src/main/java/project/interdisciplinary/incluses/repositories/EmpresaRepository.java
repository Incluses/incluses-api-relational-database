package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import project.interdisciplinary.incluses.models.Empresa;

import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

    @Procedure(procedureName = "deletar_empresa")
    void deletarEmpresa(@Param("id_empresa") UUID idEmpresa);

    @Procedure(procedureName = "criar_empresa")
    void criarEmpresa(@Param("e_cnpj") String cnpj,
                      @Param("e_razao_social") String razaoSocial,
                      @Param("e_website") String website,
                      @Param("e_setor") String setor,
                      @Param("en_rua") String rua,
                      @Param("en_estado") String estado,
                      @Param("en_cidade") String cidade,
                      @Param("en_cep") String cep,
                      @Param("en_numero") Integer numero,
                      @Param("p_nome") String nome,
                      @Param("p_senha") String senha,
                      @Param("p_email") String email,
                      @Param("t_telefone") String telefone);
}

