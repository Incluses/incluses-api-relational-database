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
import project.interdisciplinary.incluses.models.SituacaoTrabalhista;
import project.interdisciplinary.incluses.services.SituacaoTrabalhistaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/situacao-trabalhista")
public class SituacaoTrabalhistaController {
    private final SituacaoTrabalhistaService situacaoTrabalhistaService;

    @Autowired
    public SituacaoTrabalhistaController(SituacaoTrabalhistaService situacaoTrabalhistaService) {
        this.situacaoTrabalhistaService = situacaoTrabalhistaService;
    }

    @Operation(summary = "Listar todas as situações trabalhistas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de situações trabalhistas retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<SituacaoTrabalhista> listarSituacoesTrabalhistas() {
        return situacaoTrabalhistaService.listarSituacoesTrabalhistas();
    }

    @Operation(summary = "Inserir uma nova situação trabalhista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Situação trabalhista inserida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação na situação trabalhista"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirSituacaoTrabalhista(@Valid @RequestBody SituacaoTrabalhista situacaoTrabalhista, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            SituacaoTrabalhista situacaoTrabalhista1 = situacaoTrabalhistaService.salvarSituacaoTrabalhista(situacaoTrabalhista);
            if (situacaoTrabalhista1.getId() == situacaoTrabalhista.getId()) {
                Map<String, String> response = new HashMap<>();
                response.put("message","ok");
                return ResponseEntity.ok(response);
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @Operation(summary = "Atualizar uma situação trabalhista pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Situação trabalhista atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação na situação trabalhista"),
            @ApiResponse(responseCode = "404", description = "Situação trabalhista não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarSituacaoTrabalhista(@PathVariable UUID id, @Valid @RequestBody SituacaoTrabalhista situacaoTrabalhistaAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            SituacaoTrabalhista situacaoTrabalhista = situacaoTrabalhistaService.buscarSituacaoTrabalhistaPorId(id);
            situacaoTrabalhista.setNome(situacaoTrabalhistaAtualizado.getNome());
            situacaoTrabalhistaService.salvarSituacaoTrabalhista(situacaoTrabalhista);
            Map<String, String> response = new HashMap<>();
            response.put("message","ok");
            return ResponseEntity.ok(response);
        }
    }
}
