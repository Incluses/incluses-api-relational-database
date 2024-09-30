package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Vaga;
import project.interdisciplinary.incluses.services.VagaService;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

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

    @GetMapping("/selecionar")
    public List<Vaga> listarVagas() {
        return vagaService.listarVagas();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirVaga(@Valid @RequestBody Vaga vaga, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Vaga vaga1 = vagaService.salvarVaga(vaga);
            if (vaga1.getId() == vaga.getId()) {
                return ResponseEntity.ok("Inserido com sucesso");
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirVaga(@PathVariable UUID id) {
        if (vagaService.excluirVaga(id) != null) {
            return ResponseEntity.ok("Vaga excluída com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarVaga(@PathVariable UUID id, @Valid @RequestBody Vaga vagaAtualizada, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Vaga vaga = vagaService.buscarVagaPorId(id);
            vaga.setDescricao(vagaAtualizada.getDescricao());
            vaga.setFkEmpresaId(vagaAtualizada.getFkEmpresaId());
            vagaService.salvarVaga(vaga);
            return ResponseEntity.ok("Vaga atualizada com sucesso");
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarVagaParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Vaga vaga = vagaService.buscarVagaPorId(id);

        if (vaga == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("descricao")) {
            vaga.setDescricao((String) updates.get("descricao"));
        }
        if (updates.containsKey("fkTipoVaga")) {
                try {
                    vaga.setFkTipoVagaId(((UUID) updates.get("fkTipoVaga")));
                } catch (ClassCastException e) {
                    return ResponseEntity.badRequest().body("Tipo de vaga inválido.");
                }
            }
        if (updates.containsKey("fkEmpresaId")) {
                try {
                    vaga.setFkEmpresaId(((UUID) updates.get("fkEmpresaId")));
                } catch (ClassCastException e) {
                    return ResponseEntity.badRequest().body("Empresa inválida.");
                }
            }

            // Validate the updated Vaga object
        Set<ConstraintViolation<Vaga>> violations = validator.validate(vaga);
        if (!violations.isEmpty()) {
                Map<String, String> errors = new HashMap<>();
                for (ConstraintViolation<Vaga> violation : violations) {
                    errors.put(violation.getPropertyPath().toString(), violation.getMessage());
                }
                return ResponseEntity.badRequest().body(errors);
        }

        vagaService.salvarVaga(vaga);
        return ResponseEntity.ok("Vaga atualizada com sucesso");
    }
}
