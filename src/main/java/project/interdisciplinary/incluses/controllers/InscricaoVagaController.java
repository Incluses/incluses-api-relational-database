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
import project.interdisciplinary.incluses.models.InscricaoVaga;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.models.dto.CriarInscricaoVagaDTO;
import project.interdisciplinary.incluses.services.InscricaoVagaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.*;

@RestController
@RequestMapping("/inscricao-vaga")
public class InscricaoVagaController {

    private final InscricaoVagaService inscricaoVagaService;

    @Autowired
    private Validator validator;

    @Autowired
    public InscricaoVagaController(InscricaoVagaService inscricaoVagaService) {
        this.inscricaoVagaService = inscricaoVagaService;
    }

    @Operation(summary = "Listar todas as inscrições de vagas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscrições retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<InscricaoVaga> listarInscricoes() {
        return inscricaoVagaService.listarInscricaoVaga();
    }

    @Operation(summary = "Inserir uma nova inscrição em vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscrição inserida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirInscricao(@Valid @RequestBody CriarInscricaoVagaDTO inscricaoVaga, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            if (inscricaoVagaService.criarInscricaoVaga(inscricaoVaga)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Inscrição inserida com sucesso.");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Inscrição já feita anteriormente.");
                return ResponseEntity.badRequest().body(response);
            }
        }
    }

    @Operation(summary = "Encontrar inscrições de vaga por ID de usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscrições encontradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma inscrição encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-fk-usuario/{fkUsuario}")
    public Object acharCursoPorFkPerfil(@PathVariable UUID fkUsuario) {
        List<InscricaoVaga> inscricaoVagas = inscricaoVagaService.findInscricaoByFkUsuario(fkUsuario);
        if (inscricaoVagas != null) {
            return inscricaoVagas;
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Nada encontrado");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Excluir uma inscrição em vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscrição excluída com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Inscrição não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirInscricao(@PathVariable UUID id) {
        if (inscricaoVagaService.excluirInscricaoVaga(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Inscrição excluída com sucesso.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar parcialmente uma inscrição em vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscrição atualizada parcialmente com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Inscrição não encontrada"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarInscricaoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        InscricaoVaga inscricaoExistente = inscricaoVagaService.buscarInscricaoVagaPorId(id);
        Map<String, String> response = new HashMap<>();

        if (inscricaoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("fkUsuarioId")) {
            inscricaoExistente.setFkUsuarioId(UUID.fromString((String) updates.get("fkUsuarioId")));
        }
        if (updates.containsKey("fkVagaId")) {
            inscricaoExistente.setFkVagaId(UUID.fromString((String) updates.get("fkVagaId")));
        }

        // Validate the updated InscricaoVaga object
        Set<ConstraintViolation<InscricaoVaga>> violations = validator.validate(inscricaoExistente);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<InscricaoVaga> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        inscricaoVagaService.salvarInscricaoVaga(inscricaoExistente);
        response.put("message", "Inscrição atualizada parcialmente com sucesso.");
        return ResponseEntity.ok(response);
    }
}
