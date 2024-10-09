package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.models.Vaga;
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

    @GetMapping("/selecionar")
    public List<Curso> listarCursos() {
        return cursoService.listarCursos();
    }
    @GetMapping("/selecionar-nome/{nome}")
    public List<Curso> buscarCursoNome (@PathVariable String nome){
        List<Curso> cursos = cursoService.findByNome(nome);
        if (cursos != null){
            return cursos;
        }
        else {
            return null;
        }
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirCurso(@Valid @RequestBody Curso curso, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Curso curso1 = cursoService.salvarCurso(curso);
            if (curso1.getId() == curso.getId()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "ok");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirCurso(@PathVariable UUID id) {
        if (cursoService.excluirCurso(id) == true) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarCurso(@PathVariable UUID id, @Valid @RequestBody Curso cursoAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Curso curso = cursoService.buscarCursoPorId(id);
            curso.setDescricao(cursoAtualizado.getDescricao());
            curso.setNome(cursoAtualizado.getNome());
            cursoService.salvarCurso(curso);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

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

        // Validate the updated Curso object
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
        return ResponseEntity.ok(response);    }
}
