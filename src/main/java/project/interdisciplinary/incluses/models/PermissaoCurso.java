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
    @NotNull(message = "a fkCursoId não pode ser nula")
    @Column(name = "fk_curso_id")
    @Schema(description = "Fk do curso que o usuario se inscreve", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkCursoId;
    @OneToOne
    @JoinColumn(name = "fk_curso_id",nullable = false, insertable = false, updatable = false)
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

    public UUID getFkCursoId() {
        return fkCursoId;
    }

    public void setFkCursoId(UUID fkCursoId) {
        this.fkCursoId = fkCursoId;
    }
}

