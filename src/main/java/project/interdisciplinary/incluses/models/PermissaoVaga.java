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

    @ManyToOne
    @JoinColumn(name = "id_vaga")
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
}

