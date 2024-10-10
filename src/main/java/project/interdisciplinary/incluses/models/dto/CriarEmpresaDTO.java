package project.interdisciplinary.incluses.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.UUID;

public class CriarEmpresaDTO {

    @NotNull(message = "o cnpj não pode ser nulo")
    @CNPJ(message = "o cnpj deve ser válido")
    private String cnpj;

    @NotNull(message = "a razaoSocial não pode ser nula")
    @Size(max = 100)
    private String razaoSocial;
    @Size(max = 220)
    private String website;

    @NotNull(message = "o nome não pode ser nulo")
    @Size(max = 200 ,message = "O nome não pode passar de 50 caracteres")
    private String setor;

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
    @NotNull(message = "o nome não pode ser nulo")
    @Size(max = 100, message = "o nome não pode passar de 100 caracteres")
    private String nome;
    @NotNull(message = "o nome não pode ser nulo")
    @Size(max = 50, message = "a senha não pode passar de 50 caracteres")
    private String senha;

    @NotNull(message = "o email não pode ser nulo")
    @Email(message = "o email deve ser válido")
    @Size(max = 100, message = "o email não pode passar de 100 caracteres")
    private String email;

    @NotNull(message = "o telefone não pode ser nulo")
    @Size(min = 11, max = 11)
    private String telefone;

    // Getters e Setters
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

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
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

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
