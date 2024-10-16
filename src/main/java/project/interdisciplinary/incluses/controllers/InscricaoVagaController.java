package project.interdisciplinary.incluses.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.InscricaoCurso;
import project.interdisciplinary.incluses.models.InscricaoVaga;
import project.interdisciplinary.incluses.models.dto.CriarInscricaoVagaDTO;
import project.interdisciplinary.incluses.services.InscricaoVagaService;

import java.util.*;

@RestController
@RequestMapping("/inscricao-vaga")
public class InscricaoVagaController {

    private final InscricaoVagaService inscricaoVagaService;

    @Autowired
    private Validator validator;

    @Autowired
    public InscricaoVagaController(InscricaoVagaService inscricaoVagaService) {
        this.inscricaoVagaService = inscricaoVagaService;
    }

    @GetMapping("/selecionar")
    public List<InscricaoVaga> listarInscricoes() {
        return inscricaoVagaService.listarInscricaoVaga();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirInscricao(@Valid @RequestBody CriarInscricaoVagaDTO inscricaoVaga, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            if(inscricaoVagaService.criarInscricaoVaga(inscricaoVaga)){
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
    public Object acharCursoPorFkPerfil(@PathVariable UUID fkUsuario){
        List<InscricaoVaga> inscricaoVagas = inscricaoVagaService.findInscricaoByFkUsuario(fkUsuario);
        if(inscricaoVagas != null){
            return inscricaoVagas;
        }
        else {
            Map<String, String> response = new HashMap<>();
            response.put("message","nada encontrado");
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirInscricao(@PathVariable UUID id) {
        if (inscricaoVagaService.excluirInscricaoVaga(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Inscrição excluída com sucesso.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarInscricao(@PathVariable UUID id, @Valid @RequestBody InscricaoVaga inscricaoAtualizada, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            InscricaoVaga inscricaoExistente = inscricaoVagaService.buscarInscricaoVagaPorId(id);
            if (inscricaoExistente == null) {
                return ResponseEntity.notFound().build();
            }
            inscricaoExistente.setFkUsuarioId(inscricaoAtualizada.getFkUsuarioId());
            inscricaoExistente.setFkVagaId(inscricaoAtualizada.getFkVagaId());
            inscricaoVagaService.salvarInscricaoVaga(inscricaoExistente);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Inscrição atualizada com sucesso.");
            return ResponseEntity.ok(response);
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarInscricaoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        InscricaoVaga inscricaoExistente = inscricaoVagaService.buscarInscricaoVagaPorId(id);
        Map<String, String> response = new HashMap<>();

        if (inscricaoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("fkUsuarioId")) {
            inscricaoExistente.setFkUsuarioId(UUID.fromString((String) updates.get("fkUsuarioId")));
        }
        if (updates.containsKey("fkVagaId")) {
            inscricaoExistente.setFkVagaId(UUID.fromString((String) updates.get("fkVagaId")));
        }

        // Validate the updated InscricaoVaga object
        Set<ConstraintViolation<InscricaoVaga>> violations = validator.validate(inscricaoExistente);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<InscricaoVaga> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        inscricaoVagaService.salvarInscricaoVaga(inscricaoExistente);
        response.put("message", "Inscrição atualizada parcialmente com sucesso.");
        return ResponseEntity.ok(response);
    }
}
