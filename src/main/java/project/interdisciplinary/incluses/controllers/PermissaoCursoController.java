package project.interdisciplinary.incluses.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.PermissaoCurso;
import project.interdisciplinary.incluses.services.PermissaoCursoService;

import java.util.*;

@RestController
@RequestMapping("/permissao-curso")
public class PermissaoCursoController {

    private final PermissaoCursoService permissaoCursoService;

    @Autowired
    private Validator validator;

    @Autowired
    public PermissaoCursoController(PermissaoCursoService permissaoCursoService) {
        this.permissaoCursoService = permissaoCursoService;
    }

    @GetMapping("/selecionar")
    public List<PermissaoCurso> listarPermissoes() {
        return permissaoCursoService.listarPermissoesCursos();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirPermissao(@Valid @RequestBody PermissaoCurso permissaoCurso, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            PermissaoCurso novaPermissao = permissaoCursoService.salvarPermissaoCurso(permissaoCurso);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Permissão inserida com sucesso.");
            response.put("id", novaPermissao.getId().toString());
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirPermissao(@PathVariable UUID id) {
        if (permissaoCursoService.excluirPermissaoCurso(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Permissão excluída com sucesso.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarPermissao(@PathVariable UUID id, @Valid @RequestBody PermissaoCurso permissaoAtualizada, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            PermissaoCurso permissaoExistente = permissaoCursoService.buscarPermissaoPorId(id);
            if (permissaoExistente == null) {
                return ResponseEntity.notFound().build();
            }
            permissaoExistente.setPermissao(permissaoAtualizada.getPermissao());
            permissaoExistente.setFkCursoId(permissaoAtualizada.getFkCursoId());
            permissaoCursoService.salvarPermissaoCurso(permissaoExistente);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Permissão atualizada com sucesso.");
            return ResponseEntity.ok(response);
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarPermissaoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        PermissaoCurso permissaoExistente = permissaoCursoService.buscarPermissaoPorId(id);
        Map<String, String> response = new HashMap<>();

        if (permissaoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("permissao")) {
            permissaoExistente.setPermissao((Boolean) updates.get("permissao"));
        }
        if (updates.containsKey("fkCursoId")) {
            permissaoExistente.setFkCursoId(UUID.fromString((String) updates.get("fkCursoId")));
        }

        // Validate the updated PermissaoCurso object
        Set<ConstraintViolation<PermissaoCurso>> violations = validator.validate(permissaoExistente);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<PermissaoCurso> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        permissaoCursoService.salvarPermissaoCurso(permissaoExistente);
        response.put("message", "Permissão atualizada parcialmente com sucesso.");
        return ResponseEntity.ok(response);
    }
}
