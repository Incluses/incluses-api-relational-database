package project.interdisciplinary.incluses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
public class Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único do Arquivo", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID id;

    @NotNull(message = "o nome não pode ser nulo")
    @Size(max = 150, message = "o nome não pode ter mais de 150 caracteres")
    @Schema(description = "Nome do Arquivo", example = "arquivo.txt")
    private String nome;

    @Size(max = 1500, message = "a s3Url não pode ter mais de 300 caracteres")
    @Column(name = "s3_url")
    @Schema(description = "Url do Arquivo no Bucket S3", example = "https://s3.amazonaws.com/meu-bucket/imagens/imagem.jpg")
    private String s3Url;

    @Size(max = 1024, message = "a s3Key não pode ter mais de 50 caracteres")
    @Column(name = "s3_key")
    @Schema(description = "Key do Arquivo no Bucket S3", example = "arquivo.txt")
    private String s3Key;


    @Size(max = 50)
    @Schema(description = "Tamanho do arquivo", example = "100")
    @NotNull(message = "O tamanho não pode ser nulo")
    private String tamanho;

    @NotNull(message = "a fkTipoArquivoId não pode ser nula")
    @Column(name = "fk_tipo_arquivo_id")
    @Schema(description = "Fk do tipo do arquivo", example = "5a9238d7-d3e3-45fe-9de9-69353a542793")
    private UUID fkTipoArquivoId;

    @ManyToOne
    @JoinColumn(name = "fk_tipo_arquivo_id", nullable = false, insertable = false, updatable = false)
    private TipoArquivo tipoArquivo;

    // Getters and Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getTamanho() {
        return tamanho;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFkTipoArquivoId() {
        return fkTipoArquivoId;
    }

    public void setFkTipoArquivoId(UUID fkTipoArquivoId) {
        this.fkTipoArquivoId = fkTipoArquivoId;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public TipoArquivo getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(TipoArquivo tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }
}

