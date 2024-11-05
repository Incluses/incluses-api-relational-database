package project.interdisciplinary.incluses.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.models.dto.CriarCursoDTO;
import project.interdisciplinary.incluses.services.CursoService;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/curso")
public class CursoController {
    private final CursoService cursoService;
    private final Validator validator;

    @Autowired
    public CursoController(CursoService cursoService, Validator validator) {
        this.cursoService = cursoService;
        this.validator = validator;
    }

    @Operation(summary = "Lista todos os cursos", description = "Retorna uma lista de todos os cursos disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Curso.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<Curso> listarCursos() {
        return cursoService.listarCursos();
    }

    @Operation(summary = "Busca curso por nome", description = "Busca cursos que correspondem ao nome fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cursos encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Curso.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum curso encontrado")
    })
    @GetMapping("/selecionar-nome/{nome}")
    public List<Curso> buscarCursoNome(@PathVariable String nome) {
        List<Curso> cursos = cursoService.findByNome(nome);
        return cursos != null ? cursos : Collections.emptyList();
    }

    @Operation(summary = "Busca curso por nome e perfil", description = "Busca cursos que correspondem ao nome e perfil fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cursos encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Curso.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum curso encontrado")
    })
    @GetMapping("/selecionar-perfil-nome/{fkPerfil}/{nome}")
    public List<Curso> buscarCursoNome(@PathVariable("fkPerfil") UUID fkPerfil, @PathVariable("nome") String nome) {
        List<Curso> cursos = cursoService.findMyCursoByNome(fkPerfil, nome);
        return cursos != null ? cursos : Collections.emptyList();
    }

    @Operation(summary = "Insere um novo curso", description = "Insere um novo curso com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso inserido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados do curso")
    })
    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirCurso(@Valid @RequestBody CriarCursoDTO curso, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            UUID id = cursoService.criarCurso(curso);
            Map<String, String> response = new HashMap<>();
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Busca cursos por perfil", description = "Retorna todos os cursos associados ao perfil fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cursos encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Curso.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum curso encontrado")
    })
    @GetMapping("/selecionar-fk-perfil/{fkPerfil}")
    public Object acharCursoPorFkPerfil(@PathVariable UUID fkPerfil) {
        List<Curso> curso = cursoService.findByFkPerfil(fkPerfil);
        if (curso != null) {
            return curso;
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "nada encontrado");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Exclui um curso", description = "Exclui o curso com o ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso excluído com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirCurso(@PathVariable UUID id) {
        if (cursoService.excluirCurso(id)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualiza parcialmente um curso", description = "Atualiza parcialmente os dados de um curso com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
    ),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados atualizados"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarCursoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Curso curso = cursoService.buscarCursoPorId(id);

        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("descricao")) {
            curso.setDescricao((String) updates.get("descricao"));
        }
        if (updates.containsKey("nome")) {
            curso.setNome((String) updates.get("nome"));
        }

        Set<ConstraintViolation<Curso>> violations = validator.validate(curso);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Curso> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        cursoService.salvarCurso(curso);
        Map<String, String> response = new HashMap<>();
        response.put("message", "ok");
        return ResponseEntity.ok(response);
    }
}
