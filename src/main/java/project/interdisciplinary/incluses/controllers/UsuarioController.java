package project.interdisciplinary.incluses.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.models.Usuario;
import project.interdisciplinary.incluses.models.dto.CriarUsuarioDTO;
import project.interdisciplinary.incluses.services.UsuarioService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final Validator validator;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, Validator validator) {
        this.usuarioService = usuarioService;
        this.validator = validator;
    }

    @Operation(summary = "Listar todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @Operation(summary = "Buscar usuário por ID do perfil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-fk-perfil/{fkPerfil}")
    public Object acharUsuarioPorFkPerfil(@PathVariable UUID fkPerfil) {
        Usuario usuario = usuarioService.acharPorFkPerfil(fkPerfil);
        if (usuario != null) {
            return usuario;
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "nada encontrado");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Inserir um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário inserido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no usuário"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/public/inserir")
    public ResponseEntity<Object> inserirUsuario(@Valid @RequestBody CriarUsuarioDTO usuario, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            usuarioService.registrarUsuario(usuario);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Excluir um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirUsuario(@PathVariable UUID id) {
        if (usuarioService.excluirUsuario(id) == true) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar parcialmente um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no usuário"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarUsuarioParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        Map<String, String> response = new HashMap<>();

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("cpf")) {
            usuario.setCpf((String) updates.get("cpf"));
        }
        if (updates.containsKey("fkPerfilId")) {
            try {
                usuario.setFkPerfilId(UUID.fromString((String) updates.get("fkPerfilId")));
            } catch (ClassCastException e) {
                response.put("message", "Perfil inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("fkCurriculoId")) {
            try {
                usuario.setFkCurriculoId(UUID.fromString((String) updates.get("fkCurriculoId")));
            } catch (ClassCastException e) {
                response.put("message", "Currículo inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("dtNascimento")) {
            try {
                Date dtNascimento = new SimpleDateFormat("dd/MM/yyyy").parse((String) updates.get("dtNascimento"));
                usuario.setDtNascimento(dtNascimento);
            } catch (ParseException e) {
                response.put("message", "Data de nascimento inválida.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("pronomes")) {
            usuario.setPronomes((String) updates.get("pronomes"));
        }
        if (updates.containsKey("nomeSocial")) {
            usuario.setNomeSocial((String) updates.get("nomeSocial"));
        }

        // Validate the updated Usuario object
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Usuario> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        Usuario usuario2 = usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok(usuario2);
    }
}
