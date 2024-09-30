package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.lang.model.element.Name;
import java.util.UUID;

@Entity
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Id do curso", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID id;

    @NotNull(message = "a descricao não pode ser nula ")
    @Size(max = 300, message = "a descricao não pode ter mais de 100 caracteres")
    @Schema(description = "Descrição do curso", example = " Este curso oferece uma introdução abrangente" +
            " e prática sobre [tema principal do curso]. Ideal para iniciantes e profissionais" +
            " que desejam aprimorar suas habilidades, o curso cobre os conceitos fundamentais" +
            " e avançados de [área de estudo], proporcionando uma base sólida para a aplicação" +
            " prática em [contexto ou setor relevante].")
    private String descricao;

    @NotNull(message = "a fkPerfilId não pode ser nula")
    @Min(value = 0, message = "a fkPerfilId não pode conter valores negativos")
    @Column(name = "fk_perfil_id")
    @Schema(description = "Id do Perfil que criou o curso", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkPerfilId;

    @NotNull(message = "o nome não pode ser nulo")
    @Size(max = 100, message = "o nome não pode ter mais de 100 caracteres")
    @Schema(description = "Nome do curso", example = "Curso de Tecnologia")
    private String nome;
    @ManyToOne
    @JoinColumn(name = "fk_perfil_id", nullable = false, insertable = false, updatable = false)
    private Perfil perfil;

    // Getters and Setters

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFkPerfilId() {
        return fkPerfilId;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFkPerfilId(UUID fkPerfilId) {
        this.fkPerfilId = fkPerfilId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}

