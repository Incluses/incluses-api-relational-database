package project.interdisciplinary.incluses.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.Date;

public class CriarUsuarioDTO {

    @NotNull(message = "o cpf não pode ser nulo")
    @CPF(message = "o cpf deve ser válido")
    private String cpf;

    @NotNull(message = "a dtNascimento não pode ser nula")
    @Past(message = "a dtNascimento deve ser no passado")
    private Date dtNascimento;

    @NotNull(message = "os pronomes não podem ser nulos")
    private String pronomes;
    private String nomeSocial;

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
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getPronomes() {
        return pronomes;
    }

    public void setPronomes(String pronomes) {
        this.pronomes = pronomes;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
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

