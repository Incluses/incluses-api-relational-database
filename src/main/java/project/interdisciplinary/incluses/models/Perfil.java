package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Id do Perfil", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID id;

    @NotNull(message = "o nome não pode ser nulo")
    @Size(max = 100, message = "o nome não pode passar de 100 caracteres")
    @Schema(description = "Nome do perfil", example = "joaoluizsilva")
    private String nome;

    @NotNull(message = "o nome não pode ser nulo")
    @Size(max = 50, message = "a senha não pode passar de 50 caracteres")
    @Schema(description = "Senha do Perfil", example = "123456")
    private String senha;

    @NotNull(message = "o email não pode ser nulo")
    @Email(message = "o email deve ser válido")
    @Size(max = 100, message = "o email não pode passar de 100 caracteres")
    @Column(unique = true, nullable = false)
    @Schema(description = "Email do perfil", example = "enrico@gmail.com")
    private String email;

    @Size(max = 300, message = "a biografia não pode passar de 300 caracteres")
    @Schema(description = "Biografia do Perfil", example = "Estudante de TI")
    private String biografia;

    @NotNull(message = "a fkTipoPerfilId não pode ser nula")
    @Column(name = "fk_tipo_perfil_id")
    @Schema(description = "Fk que representa o tipo do perfil", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkTipoPerfilId;

    @NotNull(message = "a fkTipoPerfilId não pode ser nula")
    @Column(name = "fk_ft_perfil_id")
    @Schema(description = "Fk que representa o tipo do perfil", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkFtPerfilId;

    @ManyToOne
    @JoinColumn(name = "fk_tipo_perfil_id", insertable = false, updatable = false)
    private TipoPerfil tipoPerfil;

    @ManyToOne
    @JoinColumn(name = "fk_ft_perfil_id", insertable = false, updatable = false)
    private Arquivo fotoPerfil;

    // Getters and Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TipoPerfil getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(TipoPerfil tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public Arquivo getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Arquivo fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public UUID getFkTipoPerfilId() {
        return fkTipoPerfilId;
    }

    public void setFkTipoPerfilId(UUID fkTipoPerfilId) {
        this.fkTipoPerfilId = fkTipoPerfilId;
    }

    public UUID getFkFtPerfilId() {
        return fkFtPerfilId;
    }

    public void setFkFtPerfilId(UUID fkFtPerfilId) {
        this.fkFtPerfilId = fkFtPerfilId;
    }


}
