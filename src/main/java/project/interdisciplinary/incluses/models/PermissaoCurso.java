package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "permissao_curso")
public class PermissaoCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Id da Permissão do Curso", example = "1")
    private UUID id;

    @NotNull
    @Schema(description = "Permissão de acesso ao curso", example = "false")
    private Boolean permissao = false;

    @ManyToOne
    @JoinColumn(name = "fk_curso_id")
    @Schema(description = "Curso relacionado à permissão")
    private Curso curso;

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getPermissao() {
        return permissao;
    }

    public void setPermissao(Boolean permissao) {
        this.permissao = permissao;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}

