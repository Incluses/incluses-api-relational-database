package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.UUID;

@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Id da Empresa", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID id;

    @NotNull(message = "o cnpj não pode ser nulo")
    @CNPJ(message = "o cnpj deve ser válido")
    @Schema(description = "CNPJ da Empresa", example = "12345678912345")
    private String cnpj;

    @NotNull(message = "a razaoSocial não pode ser nula")
    @Size(max = 100)
    @Column(name = "razao_social")
    @Schema(description = "Razão social da empresa", example = "Tecnologia Digital Ltda.")
    private String razaoSocial;

    @NotNull(message = "a fkPerfilId não pode ser nula")
    @Column(name = "fk_perfil_id")
    @Schema(description = "Perfil da empresa", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkPerfilId;
    @Size(max = 220)
    @Schema(description = "Url do site da empresa", example = "https://sitetecnologia/")
    private String website;
    @NotNull(message = "a fkEnderecoId não pode ser nula")
    @Column(name = "fk_endereco_id")
    @Schema(description = "Fk do endereço da empresa", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkEnderecoId;
    @NotNull(message = "a fkSetorId não pode ser nula")
    @Column(name = "fk_setor_id")
    @Schema(description = "Fk do setor da empresa", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkSetorId;

    @ManyToOne
    @JoinColumn(name = "fk_perfil_id", nullable = false, insertable = false, updatable = false)
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name = "fk_endereco_id", nullable = false, insertable = false, updatable = false)
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "fk_setor_id", nullable = false, insertable = false, updatable = false)
    private Setor setor;
    // Getters and Setters

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public UUID getFkEnderecoId() {
        return fkEnderecoId;
    }

    public void setFkEnderecoId(UUID fkEnderecoId) {
        this.fkEnderecoId = fkEnderecoId;
    }

    public UUID getFkSetorId() {
        return fkSetorId;
    }

    public void setFkSetorId(UUID fkSetorId) {
        this.fkSetorId = fkSetorId;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }
}

