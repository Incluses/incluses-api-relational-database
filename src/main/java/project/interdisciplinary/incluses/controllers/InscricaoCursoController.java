package project.interdisciplinary.incluses.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.models.InscricaoCurso;
import project.interdisciplinary.incluses.models.dto.CriarInscricaoCursoDTO;
import project.interdisciplinary.incluses.services.InscricaoCursoService;

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

    @GetMapping("/selecionar")
    public List<InscricaoCurso> listarInscricoes() {
        return inscricaoCursoService.listarInscricoesCursos();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirInscricao(@Valid @RequestBody CriarInscricaoCursoDTO inscricaoCurso, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            if(inscricaoCursoService.criarInscricaoCurso(inscricaoCurso)){
                Map<String, String> response = new HashMap<>();
                response.put("message", "Inscrição inserida com sucesso.");
                return ResponseEntity.ok(response);
            }
            else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Inscrição já feita anteriormente.");
                return ResponseEntity.badRequest().body(response);
            }
        }
    }
    @GetMapping("/selecionar-fk-usuario/{fkUsuario}")
    public Object acharInscricaoCursoPorFkPerfil(@PathVariable UUID fkUsuario){
        List<InscricaoCurso> inscricaoCursos = inscricaoCursoService.findInscricaoByFkUsuario(fkUsuario);
        if(inscricaoCursos != null){
            return inscricaoCursos;
        }
        else {
            Map<String, String> response = new HashMap<>();
            response.put("message","nada encontrado");
            return ResponseEntity.ok(response);
        }
    }
    @GetMapping("/selecionar-fk-curso/{fkCurso}")
    public Object acharInscricaoCursoPorFkCurso(@PathVariable UUID fkCurso){
        List<InscricaoCurso> inscricaoCursos = inscricaoCursoService.findInscricaoByFkCurso(fkCurso);
        if(inscricaoCursos != null){
            return inscricaoCursos;
        }
        else {
            Map<String, String> response = new HashMap<>();
            response.put("message","nada encontrado");
            return ResponseEntity.ok(response);
        }
    }

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

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarInscricao(@PathVariable UUID id, @Valid @RequestBody InscricaoCurso inscricaoAtualizada, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            InscricaoCurso inscricaoExistente = inscricaoCursoService.buscarInscricaoCursoPorId(id);
            if (inscricaoExistente == null) {
                return ResponseEntity.notFound().build();
            }
            inscricaoExistente.setFkCursoId(inscricaoAtualizada.getFkCursoId());
            inscricaoExistente.setFkUsuarioId(inscricaoAtualizada.getFkUsuarioId());
            inscricaoCursoService.salvarInscricaoCurso(inscricaoExistente);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Inscrição atualizada com sucesso.");
            return ResponseEntity.ok(response);
        }
    }

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
