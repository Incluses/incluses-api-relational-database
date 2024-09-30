package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "tipo_vaga")
public class TipoVaga {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Id do TipoVaga", example = "1")
    private UUID id;
    @NotNull(message = "o nome não pode ser nulo")
    @Size(max = 50, message = "O nome não pode passar de 50 caracteres")
    @Schema(description = "Nome do TipoVaga", example = "TI")
    private String nome;

    // Getters and Setters


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
}
