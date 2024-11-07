package project.interdisciplinary.incluses.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.MaterialCurso;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.models.dto.CriarMaterialCursoDTO;
import project.interdisciplinary.incluses.services.MaterialCursoService;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/material-curso")
public class MaterialCursoController {
    private final MaterialCursoService materialCursoService;
    private final Validator validator;

    @Autowired
    public MaterialCursoController(MaterialCursoService materialCursoService, Validator validator) {
        this.materialCursoService = materialCursoService;
        this.validator = validator;
    }

    @Operation(summary = "Listar todos os materiais de cursos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materiais retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<MaterialCurso> listarMateriaisCursos() {
        return materialCursoService.listarMateriaisCursos();
    }

    @Operation(summary = "Buscar materiais por ID do curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materiais do curso encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-fk-curso/{fkCurso}")
    public List<MaterialCurso> buscarMaterialDoCurso(@PathVariable UUID fkCurso) {
        List<MaterialCurso> materialCursos = materialCursoService.findMaterialByFkCurso(fkCurso);
        return materialCursos != null ? materialCursos : new ArrayList<>();
    }

    @Operation(summary = "Buscar materiais por nome e ID do curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materiais encontrados com sucesso",                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Materiais não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-nome/{nome}/{fkCurso}")
    public List<MaterialCurso> buscarMaterialPorNome(@PathVariable("nome") String nome, @PathVariable("fkCurso") UUID fkCurso) {
        List<MaterialCurso> materialCursos = materialCursoService.findMaterialByNome(fkCurso, nome);
        return materialCursos != null ? materialCursos : new ArrayList<>();
    }

    @Operation(summary = "Inserir um novo material de curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material inserido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirMaterialCurso(@Valid @RequestBody CriarMaterialCursoDTO materialCurso, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            materialCursoService.criarMaterialCurso(materialCurso);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }



    @Operation(summary = "Atualizar parcialmente um material de curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material atualizado parcialmente com sucesso"),
            @ApiResponse(responseCode = "404", description = "Material não encontrado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarMaterialCursoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        MaterialCurso materialCurso = materialCursoService.buscarMaterialCursoPorId(id);
        Map<String, String> response = new HashMap<>();

        if (materialCurso == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("nome")) {
            materialCurso.setNome((String) updates.get("nome"));
        }
        if (updates.containsKey("fkCursoId")) {
            try {
                materialCurso.setFkCursoId(UUID.fromString((String) updates.get("fkCursoId")));
            } catch (ClassCastException e) {
                response.put("message", "Curso inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("fkArquivoId")) {
            try {
                materialCurso.setFkArquivoId(UUID.fromString((String) updates.get("fkArquivoId")));
            } catch (ClassCastException e) {
                response.put("message", "Arquivo inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("descricao")) {
            materialCurso.setDescricao((String) updates.get("descricao"));
        }

        // Validate the updated MaterialCurso object
        Set<ConstraintViolation<MaterialCurso>> violations = validator.validate(materialCurso);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<MaterialCurso> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        materialCursoService.salvarMaterialCurso(materialCurso);
        response.put("message", "ok");
        return ResponseEntity.ok(response);
    }
}
