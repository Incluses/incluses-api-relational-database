package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "inscricao_curso")
public class InscricaoCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID da inscrição do Curso", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID id;

    @NotNull(message = "a fkCursoId não pode ser nula")
    @Column(name = "fk_curso_id")
    @Schema(description = "Fk do curso que o usuario se inscreve", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkCursoId;

    @NotNull(message = "a fkUsuarioId não pode ser nula")
    @Column(name = "fk_usuario_id")
    @Schema(description = "Fk do usuário que se inscreve no curso", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkUsuarioId;
    
    @ManyToOne
    @JoinColumn(name = "fk_curso_id", nullable = false, insertable = false, updatable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "fk_usuario_id", nullable = false, insertable = false, updatable = false)
    private Usuario usuario;

    // Getters and Setters


    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFkCursoId() {
        return fkCursoId;
    }

    public void setFkCursoId(UUID fkCursoId) {
        this.fkCursoId = fkCursoId;
    }

    public UUID getFkUsuarioId() {
        return fkUsuarioId;
    }

    public void setFkUsuarioId(UUID fkUsuarioId) {
        this.fkUsuarioId = fkUsuarioId;
    }
}

