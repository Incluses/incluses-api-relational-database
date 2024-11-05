package project.interdisciplinary.incluses.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.AvaliacaoCurso;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.models.Usuario;
import project.interdisciplinary.incluses.services.AvaliacaoCursoService;

import java.util.*;

@RestController
@RequestMapping("/avaliacao-curso")
public class AvaliacaoCursoController {

    private final AvaliacaoCursoService avaliacaoCursoService;

    @Autowired
    private Validator validator;

    @Autowired
    public AvaliacaoCursoController(AvaliacaoCursoService avaliacaoCursoService) {
        this.avaliacaoCursoService = avaliacaoCursoService;
    }

    @Operation(summary = "Lista todas as avaliações de cursos", description = "Retorna uma lista de todas as avaliações de cursos disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvaliacaoCurso.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<AvaliacaoCurso> listarAvaliacoes() {
        return avaliacaoCursoService.listarAvaliacoes();
    }

    @Operation(summary = "Lista avaliações de cursos por usuário", description = "Retorna avaliações de cursos por ID do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvaliacaoCurso.class))),
            @ApiResponse(responseCode = "404", description = "Avaliações não encontradas para o usuário"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-fk-usuario/{fkUsuario}")
    public Object acharAvaliacoesPorFkUsuario(@PathVariable UUID fkUsuario){
        List<AvaliacaoCurso> avaliacaoCurso = avaliacaoCursoService.acharPorFkUser(fkUsuario);
        if(avaliacaoCurso != null){
            return avaliacaoCurso;
        }
        else {
            return ResponseEntity.notFound();
        }
    }

    @Operation(summary = "Lista avaliações de cursos por curso", description = "Retorna avaliações de cursos por ID do curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvaliacaoCurso.class))),
            @ApiResponse(responseCode = "404", description = "Avaliações não encontradas para o curso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-fk-curso/{fkCurso}")
    public Object acharAvaliacoesPorFkCurso(@PathVariable UUID fkCurso){
        List<AvaliacaoCurso> avaliacaoCurso = avaliacaoCursoService.acharPorFkCurso(fkCurso);
        if(avaliacaoCurso != null){
            return avaliacaoCurso;
        }
        else {
            Map<String, String> response = new HashMap<>();
            response.put("message","nada encontrado");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Insere uma avaliação de curso", description = "Insere uma avaliação de curso com base nos dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação inserida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvaliacaoCurso.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirAvaliacao(@Valid @RequestBody AvaliacaoCurso avaliacaoCurso, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            AvaliacaoCurso novaAvaliacao = avaliacaoCursoService.salvarAvaliacao(avaliacaoCurso);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Avaliação inserida com sucesso.");
            response.put("id", novaAvaliacao.getId().toString());
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Exclui uma avaliação de curso", description = "Exclui uma avaliação de curso com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação excluída com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirAvaliacao(@PathVariable UUID id) {
        if (avaliacaoCursoService.excluirAvaliacao(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Avaliação excluída com sucesso.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualiza parcialmente uma avaliação de curso", description = "Atualiza parcialmente uma avaliação de curso com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada parcialmente com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarAvaliacaoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        AvaliacaoCurso avaliacaoExistente = avaliacaoCursoService.buscarAvaliacaoPorId(id);
        Map<String, String> response = new HashMap<>();

        if (avaliacaoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("nota")) {
            avaliacaoExistente.setNota(Double.parseDouble(updates.get("nota").toString()));
        }

        Set<ConstraintViolation<AvaliacaoCurso>> violations = validator.validate(avaliacaoExistente);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<AvaliacaoCurso> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        avaliacaoCursoService.salvarAvaliacao(avaliacaoExistente);
        response.put("message", "Avaliação atualizada parcialmente com sucesso.");
        return ResponseEntity.ok(response);
    }
}
