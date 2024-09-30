package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "material_curso")
public class MaterialCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID do material do curso", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID id;

    @NotNull(message = "o nome não pode ser nulo")
    @Size(max = 255, message = "o nome não pode ter mais de 255 caracteres")
    @Schema(description = "Nome do curso", example = "Aula 1")
    private String nome;

    @NotNull(message = "a fkCursoId não pode ser nula")
    @Column(name = "fk_curso_id")
    @Schema(description = "Fk do curso que o material pertence", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkCursoId;

    @NotNull(message = "a fkArquivoId não pode ser nula")
    @Column(name = "fk_arquivo_id")
    @Schema(description = "Fk do arquivo que o dono do curso insere no material", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkArquivoId;


    @ManyToOne
    @JoinColumn(name = "fk_curso_id", nullable = false, insertable = false, updatable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "fk_arquivo_id", nullable = false, insertable = false, updatable = false)
    private Arquivo arquivo;

    @NotNull(message = "a descricao não pode ser nula")
    @Size(max = 300, message = "a descricao não pode ter mais de 100 caracteres")
    @Schema(description = "Descrição do material", example = "Nessa aula veremos conteúdo X, segue anexo.")
    private String descricao;

    // Getters and Setters


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

    public UUID getFkArquivoId() {
        return fkArquivoId;
    }

    public void setFkArquivoId(UUID fkArquivoId) {
        this.fkArquivoId = fkArquivoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }
}

