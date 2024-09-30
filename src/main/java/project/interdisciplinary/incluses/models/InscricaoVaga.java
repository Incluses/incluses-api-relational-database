package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
public class InscricaoVaga {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID da inscrição da vaga", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID id;

    @NotNull(message = "O fkUsuarioId não pode ser nulo")
    @Column(name = "fk_usuario_id", nullable = false)
    @Schema(description = "ID do usuário", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkUsuarioId;

    @NotNull(message = "O fkVagaId não pode ser nulo")
    @Column(name = "fk_vaga_id", nullable = false)
    @Schema(description = "ID da vaga", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkVagaId;

    @ManyToOne
    @JoinColumn(name = "fk_usuario_id", insertable = false, updatable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "fk_vaga_id", insertable = false, updatable = false)
    private Vaga vaga;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFkUsuarioId() {
        return fkUsuarioId;
    }

    public void setFkUsuarioId(UUID fkUsuarioId) {
        this.fkUsuarioId = fkUsuarioId;
    }

    public UUID getFkVagaId() {
        return fkVagaId;
    }

    public void setFkVagaId(UUID fkVagaId) {
        this.fkVagaId = fkVagaId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }
}
