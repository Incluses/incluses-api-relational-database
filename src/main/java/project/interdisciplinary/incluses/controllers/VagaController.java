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
import project.interdisciplinary.incluses.models.Vaga;
import project.interdisciplinary.incluses.models.dto.CriarVagaDTO;
import project.interdisciplinary.incluses.services.VagaService;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/vaga")
public class VagaController {
    private final VagaService vagaService;
    private final Validator validator;

    @Autowired
    public VagaController(VagaService vagaService, Validator validator) {
        this.vagaService = vagaService;
        this.validator = validator;
    }

    @Operation(summary = "Listar todas as vagas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vagas retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<Vaga> listarVagas() {
        return vagaService.listarVagas();
    }

    @Operation(summary = "Buscar vaga pelo nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vagas encontradas pelo nome"),
            @ApiResponse(responseCode = "404", description = "Nenhuma vaga encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-nome/{nome}")
    public List<Vaga> buscarVagaNome(@PathVariable String nome) {
        List<Vaga> vagas = vagaService.findByNome(nome);
        if (vagas != null) {
            return vagas;
        } else {
            return null;
        }
    }

    @Operation(summary = "Buscar vaga pelo nome do tipo de vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vagas encontradas pelo nome do tipo"),
            @ApiResponse(responseCode = "404", description = "Nenhuma vaga encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-nome-tipo/{nome}")
    public List<Vaga> buscarVagaTipoVagaNome(@PathVariable String nome) {
        List<Vaga> vagas = vagaService.findByTipoVagaNome(nome);
        if (vagas != null) {
            return vagas;
        } else {
            return null;
        }
    }

    @Operation(summary = "Buscar vaga pela ID da empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vagas encontradas pela ID da empresa"),
            @ApiResponse(responseCode = "404", description = "Nenhuma vaga encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-fk-empresa/{fkEmpresa}")
    public List<Vaga> buscarVagaFkEmpresa(@PathVariable UUID fkEmpresa) {
        List<Vaga> vagas = vagaService.findByFkEmpresa(fkEmpresa);
        if (vagas != null) {
            return vagas;
        } else {
            return null;
        }
    }

    @Operation(summary = "Inserir uma nova vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga inserida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação na vaga"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirVaga(@Valid @RequestBody CriarVagaDTO vaga, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            vagaService.criarVaga(vaga);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Excluir uma vaga pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga excluída com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Vaga não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirVaga(@PathVariable UUID id) {
        if (vagaService.excluirVaga(id) == true) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar parcialmente uma vaga pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação na vaga"),
            @ApiResponse(responseCode = "404", description = "Vaga não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarVagaParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Vaga vaga = vagaService.buscarVagaPorId(id);
        Map<String, String> response = new HashMap<>();

        if (vaga == null) {
            return ResponseEntity.notFound().build();
        }

        // Atualizar campos com base nas chaves presentes em `updates`
        if (updates.containsKey("descricao")) {
            vaga.setDescricao((String) updates.get("descricao"));
        }
        if (updates.containsKey("nome")) {
            vaga.setNome((String) updates.get("nome"));
        }
        if (updates.containsKey("fkEmpresaId")) {
            try {
                vaga.setFkEmpresaId(UUID.fromString((String) updates.get("fkEmpresaId")));
            } catch (ClassCastException | IllegalArgumentException e) {
                response.put("message", "Empresa inválida.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("fkTipoVagaId")) {
            try {
                vaga.setFkTipoVagaId(UUID.fromString((String) updates.get("fkTipoVagaId")));
            } catch (ClassCastException | IllegalArgumentException e) {
                response.put("message", "Tipo de vaga inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        // Validar o objeto Vaga atualizado
        Set<ConstraintViolation<Vaga>> violations = validator.validate(vaga);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Vaga> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // Salvar o objeto atualizado
        vagaService.salvarVaga(vaga);
        response.put("message", "ok");
        return ResponseEntity.ok(response);
    }
}
