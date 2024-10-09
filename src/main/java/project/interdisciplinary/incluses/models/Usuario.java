package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;
import java.util.UUID;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Id do Usuário", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID id;

    @NotNull(message = "o cpf não pode ser nulo")
    @CPF(message = "o cpf deve ser válido")
    @Schema(description = "Cpf do usuário", example = "54504078821")
    @Column(unique = true, nullable = false)
    private String cpf;

    @NotNull(message = "a fkPerfilId não pode ser nula")
    @Column(name = "fk_perfil_id")
    @Schema(description = "Fk do perfil que esse usuário se refere", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkPerfilId;
    @Column(name = "fk_curriculo_id")
    @Schema(description = "Fk do arquivo de currículo do usuário", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkCurriculoId;

    @NotNull(message = "a dtNascimento não pode ser nula")
    @Past(message = "a dtNascimento deve ser no passado")
    @Column(name = "dt_nascimento")
    @Schema(description = "Data de nascimento do usuário", example = "17/09/2002")
    private Date dtNascimento;

    @NotNull(message = "os pronomes não podem ser nulos")
    @Schema(description = "pronomes do usuário", example = "ele/dele")
    private String pronomes;

    @Schema(description = "nome_social ", example = "54504078821")
    @Column(name = "nome_social")
    private String nomeSocial;

    @ManyToOne
    @JoinColumn(name = "fk_perfil_id", nullable = false, insertable = false, updatable = false)
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name = "fk_curriculo_id", insertable = false, updatable = false)
    private Arquivo curriculo;


    // Getters and Setters

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Arquivo getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(Arquivo curriculo) {
        this.curriculo = curriculo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFkPerfilId() {
        return fkPerfilId;
    }

    public void setFkPerfilId(UUID fkPerfilId) {
        this.fkPerfilId = fkPerfilId;
    }

    public UUID getFkCurriculoId() {
        return fkCurriculoId;
    }

    public void setFkCurriculoId(UUID fkCurriculoId) {
        this.fkCurriculoId = fkCurriculoId;
    }

    public String getPronomes() {
        return pronomes;
    }

    public void setPronomes(String pronomes) {
        this.pronomes = pronomes;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }
}

