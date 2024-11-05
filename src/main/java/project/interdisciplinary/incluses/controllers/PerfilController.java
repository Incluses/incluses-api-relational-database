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
import project.interdisciplinary.incluses.models.Perfil;
import project.interdisciplinary.incluses.services.PerfilService;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/perfil")
public class PerfilController {
    private final PerfilService perfilService;
    private final Validator validator;

    @Autowired
    public PerfilController(PerfilService perfilService, Validator validator) {
        this.perfilService = perfilService;
        this.validator = validator;
    }

    @Operation(summary = "Listar todos os perfis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de perfis retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<Perfil> listarPerfis() {
        return perfilService.listarPerfis();
    }

    @Operation(summary = "Listar perfil por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-id/{id}")
    public Perfil listarPerfilPorId(@PathVariable UUID id) {
        Perfil perfil = perfilService.buscarPerfilPorId(id);
        if (perfil != null) {
            return perfil;
        } else {
            return null;
        }
    }

    @Operation(summary = "Buscar perfil por e-mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-email/{email}")
    public Perfil buscarPerfil(@PathVariable String email) {
        Perfil perfil = perfilService.findByEmail(email);
        if (perfil != null) {
            return perfil;
        } else {
            return null;
        }
    }

    @Operation(summary = "Buscar perfis por nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfis encontrados"),
            @ApiResponse(responseCode = "404", description = "Perfis não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-nome/{nome}")
    public List<Perfil> buscarPerfilNome(@PathVariable String nome) {
        List<Perfil> perfils = perfilService.findByNome(nome);
        if (perfils != null) {
            return perfils;
        } else {
            return null;
        }
    }


    @Operation(summary = "Atualizar parcialmente perfil por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil atualizado parcialmente com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Perfil.class))
            ),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarPerfilParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Perfil perfil = perfilService.buscarPerfilPorId(id);
        Map<String, String> response = new HashMap<>();

        if (perfil == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("nome")) {
            perfil.setNome((String) updates.get("nome"));
        }
        if (updates.containsKey("senha")) {
            perfil.setSenha((String) updates.get("senha"));
        }
        if (updates.containsKey("email")) {
            perfil.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("biografia")) {
            perfil.setBiografia((String) updates.get("biografia"));
        }
        if (updates.containsKey("fkTipoPerfilId")) {
            try {
                perfil.setFkTipoPerfilId(UUID.fromString((String) updates.get("fkTipoPerfilId")));
            } catch (ClassCastException e) {
                response.put("message", "Tipo de perfil inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("fkFtPerfilId")) {
            try {
                perfil.setFkFtPerfilId(UUID.fromString((String) updates.get("fkFtPerfilId")));
            } catch (ClassCastException e) {
                response.put("message", "Foto de perfil inválida.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        // Validate the updated Perfil object
        Set<ConstraintViolation<Perfil>> violations = validator.validate(perfil);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Perfil> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        Perfil perfil2 = perfilService.salvarPerfil(perfil); // Supondo que você tenha um método para salvar o Perfil
        return ResponseEntity.ok(perfil2);
    }
}
