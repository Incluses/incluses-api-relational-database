package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Id do Telefone", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID id;

    @NotNull(message = "o telefone não pode ser nulo")
    @Size(min = 11, max = 11)
    @Schema(description = "Número de telefone", example = "11976430875")
    private String telefone;

    @NotNull(message = "a fkPerfilId não pode ser nula")
    @Min(value = 0, message = "a fkPerfilId não pode conter valores negativos")
    @Column(name = "fk_perfil_id")
    @Schema(description = "Fk do perfil que o telefone pertence", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkPerfilId;

    @ManyToOne
    @JoinColumn(name = "fk_perfil_id", nullable = false, insertable = false, updatable = false)
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

    public UUID getFkPerfilId() {
        return fkPerfilId;
    }

    public void setFkPerfilId(UUID fkPerfilId) {
        this.fkPerfilId = fkPerfilId;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

}

