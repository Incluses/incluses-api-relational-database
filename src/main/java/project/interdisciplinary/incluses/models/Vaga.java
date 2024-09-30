package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Id da Vaga", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID id;

    @NotNull(message = "a descricao não pode ser nula")
    @Size(max = 300, message = "a descricao não pode ter mais de 100 caracteres")
    @Schema(description = "Descrição da Vaga",
            example = "Buscamos profissional proativo para atuar em equipe dinâmica, com foco" +
            " em resultados e inovação.")
    private String descricao;

    @NotNull(message = "o nome não pode ser nulo")
    @Size(max = 100, message = "o nome não pode passar de 100 caracteres")
    @Schema(description = "nome da vaga", example = "Programador Sênior")
    private String nome;

    @NotNull(message = "a fkEmpresaId não pode ser nula")
    @Column(name = "fk_Empresa_id")
    @Schema(description = "Fk da empresa que disponibiliza a vaga", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkEmpresaId;

    @NotNull(message = "a fkTipoVaga não pode ser nula")
    @Column(name = "fk_tipo_vaga_id")
    @Schema(description = "Fk do tipo da vaga", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkTipoVagaId;

    @ManyToOne
    @JoinColumn(name = "fk_empresa_id", nullable = false, insertable = false, updatable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "fk_tipo_vaga_id", nullable = false, insertable = false, updatable = false)
    private TipoVaga tipoVaga;


    // Getters and Setters


    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public TipoVaga getTipoVaga() {
        return tipoVaga;
    }

    public void setTipoVaga(TipoVaga tipoVaga) {
        this.tipoVaga = tipoVaga;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UUID getFkEmpresaId() {
        return fkEmpresaId;
    }

    public void setFkEmpresaId(UUID fkEmpresaId) {
        this.fkEmpresaId = fkEmpresaId;
    }

    public UUID getFkTipoVagaId() {
        return fkTipoVagaId;
    }

    public void setFkTipoVagaId(UUID fkTipoVagaId) {
        this.fkTipoVagaId = fkTipoVagaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


}

