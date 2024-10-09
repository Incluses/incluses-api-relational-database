package project.interdisciplinary.incluses.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.AvaliacaoCurso;
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

    @GetMapping("/selecionar")
    public List<AvaliacaoCurso> listarAvaliacoes() {
        return avaliacaoCursoService.listarAvaliacoes();
    }

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

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarAvaliacao(@PathVariable UUID id, @Valid @RequestBody AvaliacaoCurso avaliacaoAtualizada, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            AvaliacaoCurso avaliacaoExistente = avaliacaoCursoService.buscarAvaliacaoPorId(id);
            if (avaliacaoExistente == null) {
                return ResponseEntity.notFound().build();
            }
            avaliacaoExistente.setNota(avaliacaoAtualizada.getNota());
            avaliacaoExistente.setFkCursoId(avaliacaoAtualizada.getFkCursoId());
            avaliacaoExistente.setFkUsuarioId(avaliacaoAtualizada.getFkUsuarioId());
            avaliacaoCursoService.salvarAvaliacao(avaliacaoExistente);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Avaliação atualizada com sucesso.");
            return ResponseEntity.ok(response);
        }
    }

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
        if (updates.containsKey("fkCursoId")) {
            avaliacaoExistente.setFkCursoId(UUID.fromString((String) updates.get("fkCursoId")));
        }
        if (updates.containsKey("fkUsuarioId")) {
            avaliacaoExistente.setFkUsuarioId(UUID.fromString((String) updates.get("fkUsuarioId")));
        }

        // Validate the updated AvaliacaoCurso object
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