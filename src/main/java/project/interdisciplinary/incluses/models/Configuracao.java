package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Id da configuracao", example = "1")
    private UUID id;

    @NotNull(message = "a notificacao não pode ser nula")
    @Schema(description = "Ver se a notificação está ativada ou não", example = "True")
    private Boolean notificacao;

    @NotNull(message = "a fkPerfilId não pode ser nula")
    @Column(name = "fk_perfil_id")
    @Schema(description = "Fk do perfil que a configuração se referencia", example = "2")
    private UUID fkPerfilId;

    @ManyToOne
    @JoinColumn(name = "fk_perfil_id", nullable = false , insertable = false, updatable = false)
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

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(Boolean notificacao) {
        this.notificacao = notificacao;
    }

    public void setFkPerfilId(UUID fkPerfilId) {
        this.fkPerfilId = fkPerfilId;
    }

    public UUID getFkPerfilId() {
        return fkPerfilId;
    }
}

