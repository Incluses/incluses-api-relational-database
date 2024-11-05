package project.interdisciplinary.incluses.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.models.PermissaoVaga;
import project.interdisciplinary.incluses.services.PermissaoVagaService;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/permissao-vaga")
public class PermissaoVagaController {

    private final PermissaoVagaService permissaoVagaService;

    @Autowired
    private Validator validator;

    @Autowired
    public PermissaoVagaController(PermissaoVagaService permissaoVagaService) {
        this.permissaoVagaService = permissaoVagaService;
    }

    @Operation(summary = "Listar todas as permissões de vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de permissões retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<PermissaoVaga> listarPermissoes() {
        return permissaoVagaService.listarPermissaoVaga();
    }

    @Operation(summary = "Inserir nova permissão de vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissão inserida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação na permissão"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirPermissao(@Valid @RequestBody PermissaoVaga permissaoVaga, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            PermissaoVaga novaPermissao = permissaoVagaService.salvarPermissaoVaga(permissaoVaga);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Permissão inserida com sucesso.");
            response.put("id", novaPermissao.getId().toString());
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Excluir permissão de vaga por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissão excluída com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Permissão não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirPermissao(@PathVariable UUID id) {
        if (permissaoVagaService.excluirPermissaoVaga(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Permissão excluída com sucesso.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar parcialmente permissão de vaga por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissão atualizada parcialmente com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Permissão não encontrada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação na permissão"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarPermissaoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        PermissaoVaga permissaoExistente = permissaoVagaService.buscarPermissaoVagaPorId(id);
        Map<String, String> response = new HashMap<>();

        if (permissaoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("permissao")) {
            permissaoExistente.setPermissao((Boolean) updates.get("permissao"));
        }
        if (updates.containsKey("fkVagaId")) {
            permissaoExistente.setFkVagaId(UUID.fromString((String) updates.get("fkVagaId")));
        }

        // Validate the updated PermissaoVaga object
        Set<ConstraintViolation<PermissaoVaga>> violations = validator.validate(permissaoExistente);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<PermissaoVaga> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        permissaoVagaService.salvarPermissaoVaga(permissaoExistente);
        response.put("message", "Permissão atualizada parcialmente com sucesso.");
        return ResponseEntity.ok(response);
    }
}
