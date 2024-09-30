package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "avaliacao_curso")
public class AvaliacaoCurso {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único da AvaliacaoCurso", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID id;

    @NotNull(message = "a nota não pode ser nulo")
    @Min(value = 0, message = "o valor da avaliação deve ser positivo")
    @Schema(description = "Nota do curso", example = "3")
    private double nota;

    @NotNull(message = "a fkCursoId não pode ser nula")
    @Column(name = "fk_curso_id")
    @Schema(description = "Fk do curso avaliado", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkCursoId;
    @NotNull(message = "a fkUsuarioId não pode ser nula")
    @Column(name = "fk_usuario_id")
    @Schema(description = "Fk do usuario que avaliou", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkUsuarioId;

    @ManyToOne
    @JoinColumn(name = "fk_curso_id", nullable = false, insertable = false, updatable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "fk_usuario_id", nullable = false, insertable = false, updatable = false)
    private Usuario usuario;

    // Construtores
    public AvaliacaoCurso() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
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

    public AvaliacaoCurso(double nota, Curso curso, Usuario usuario) {
        this.nota = nota;
        this.curso = curso;
        this.usuario = usuario;
    }

    // Getters e Setters


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
}
