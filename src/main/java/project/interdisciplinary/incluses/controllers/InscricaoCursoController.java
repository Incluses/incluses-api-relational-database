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
import project.interdisciplinary.incluses.models.InscricaoCurso;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.models.dto.CriarInscricaoCursoDTO;
import project.interdisciplinary.incluses.services.InscricaoCursoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.*;

@RestController
@RequestMapping("/inscricao-curso")
public class InscricaoCursoController {

    private final InscricaoCursoService inscricaoCursoService;

    @Autowired
    private Validator validator;

    @Autowired
    public InscricaoCursoController(InscricaoCursoService inscricaoCursoService) {
        this.inscricaoCursoService = inscricaoCursoService;
    }

    @Operation(summary = "Listar todas as inscrições de cursos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscrições retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<InscricaoCurso> listarInscricoes() {
        return inscricaoCursoService.listarInscricoesCursos();
    }

    @Operation(summary = "Inserir uma nova inscrição em curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscrição inserida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirInscricao(@Valid @RequestBody CriarInscricaoCursoDTO inscricaoCurso, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            if (inscricaoCursoService.criarInscricaoCurso(inscricaoCurso)) {
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

    @Operation(summary = "Encontrar inscrições de curso por ID de usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscrições encontradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma inscrição encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-fk-usuario/{fkUsuario}")
    public Object acharInscricaoCursoPorFkPerfil(@PathVariable UUID fkUsuario) {
        List<InscricaoCurso> inscricaoCursos = inscricaoCursoService.findInscricaoByFkUsuario(fkUsuario);
        if (inscricaoCursos != null) {
            return inscricaoCursos;
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Nada encontrado");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Encontrar inscrições de curso por ID de curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscrições encontradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma inscrição encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-fk-curso/{fkCurso}")
    public Object acharInscricaoCursoPorFkCurso(@PathVariable UUID fkCurso) {
        List<InscricaoCurso> inscricaoCursos = inscricaoCursoService.findInscricaoByFkCurso(fkCurso);
        if (inscricaoCursos != null) {
            return inscricaoCursos;
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Nada encontrado");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Contar inscrições de curso por ID de curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número de inscrições retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma inscrição encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-acessos/{fkCurso}")
    public Object contarInscricoesCurso(@PathVariable UUID fkCurso) {
        List<InscricaoCurso> inscricaoCursos = inscricaoCursoService.findInscricaoByFkCurso(fkCurso);
        Map<String, Integer> response = new HashMap<>();
        response.put("number", inscricaoCursos != null ? inscricaoCursos.size() : 0);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Excluir uma inscrição em curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscrição excluída com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Inscrição não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirInscricao(@PathVariable UUID id) {
        if (inscricaoCursoService.excluirInscricaoCurso(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Inscrição excluída com sucesso.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar parcialmente uma inscrição em curso")
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
        InscricaoCurso inscricaoExistente = inscricaoCursoService.buscarInscricaoCursoPorId(id);
        Map<String, String> response = new HashMap<>();

        if (inscricaoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("fkCursoId")) {
            inscricaoExistente.setFkCursoId(UUID.fromString((String) updates.get("fkCursoId")));
        }
        if (updates.containsKey("fkUsuarioId")) {
            inscricaoExistente.setFkUsuarioId(UUID.fromString((String) updates.get("fkUsuarioId")));
        }

        // Validate the updated InscricaoCurso object
        Set<ConstraintViolation<InscricaoCurso>> violations = validator.validate(inscricaoExistente);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<InscricaoCurso> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        inscricaoCursoService.salvarInscricaoCurso(inscricaoExistente);
        response.put("message", "Inscrição atualizada parcialmente com sucesso.");
        return ResponseEntity.ok(response);
    }
}
