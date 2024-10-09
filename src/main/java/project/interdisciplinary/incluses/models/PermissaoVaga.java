package project.interdisciplinary.incluses.models;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "permissao_vaga")
public class PermissaoVaga {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Id da Permissão da Vaga", example = "1")
    private UUID id;

    @NotNull
    @Schema(description = "Permissão de acesso à vaga", example = "false")
    private Boolean permissao = false;

    @NotNull(message = "O fkVagaId não pode ser nulo")
    @Column(name = "fk_vaga_id", nullable = false)
    @Schema(description = "ID da vaga", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkVagaId;
    @ManyToOne
    @JoinColumn(name = "id_vaga",nullable = false, insertable = false, updatable = false)
    @Schema(description = "Vaga relacionada à permissão")
    private Vaga vaga;

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

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    public UUID getFkVagaId() {
        return fkVagaId;
    }

    public void setFkVagaId(UUID fkVagaId) {
        this.fkVagaId = fkVagaId;
    }
}

