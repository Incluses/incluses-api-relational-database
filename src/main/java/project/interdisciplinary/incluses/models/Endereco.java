package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "id do Endereço", example = "5a9238d7-d3e3-45fe-9de9-69353a542793/")
    private UUID id;
    @NotNull(message = "a rua não pode ser nula")
    @Size(max = 200, message = "a rua não pode passar de 200 caracteres")
    private String rua;

    @NotNull(message = "o estado não pode ser nulo")
    @Size(max = 100, message = "o estado não pode passar de 100 caracteres")
    private String estado;
    @NotNull(message = "a cidade não pode ser nula")
    @Size(max = 100, message = "a cidade não pode passar de 100 caracteres")
    private String cidade;
    @NotNull(message = "o cep não pode ser nulo")
    @Size(max = 8, min = 8, message = "o cep deve ter 8 caracteres")
    private String cep;
    @NotNull(message = "O número não pode ser nulo")
    @Min(value = 0, message = "o numero não pode conter valores negativos")
    private Integer numero;

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

//    public String getCidade() {
//        return cidade;
//    }
//
//    public void setCidade(String cidade) {
//        this.cidade = cidade;
//    }
}
