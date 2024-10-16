package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Perfil;
import project.interdisciplinary.incluses.models.Vaga;
import project.interdisciplinary.incluses.models.dto.CriarVagaDTO;
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

    @GetMapping("/selecionar-nome/{nome}")
    public List<Vaga> buscarVagaNome (@PathVariable String nome){
        List<Vaga> vagas = vagaService.findByNome(nome);
        if (vagas != null){
            return vagas;
        }
        else {
            return null;
        }
    }
    @GetMapping("/selecionar-nome-tipo/{nome}")
    public List<Vaga> buscarVagaTipoVagaNome (@PathVariable String nome){
        List<Vaga> vagas = vagaService.findByTipoVagaNome(nome);
        if (vagas != null){
            return vagas;
        }
        else {
            return null;
        }
    }

    @GetMapping("/selecionar-fk-empresa/{fkEmpresa}")
    public List<Vaga> buscarVagaFkEmpresa (@PathVariable UUID fkEmpresa){
        List<Vaga> vagas = vagaService.findByFkEmpresa(fkEmpresa);
        if (vagas != null){
            return vagas;
        }
        else {
            return null;
        }
    }



    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirVaga(@Valid @RequestBody CriarVagaDTO vaga, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            vagaService.criarVaga(vaga);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirVaga(@PathVariable UUID id) {
        if (vagaService.excluirVaga(id) == true) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
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
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarVagaParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Vaga vaga = vagaService.buscarVagaPorId(id);
        Map<String, String> response = new HashMap<>();

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
                    response.put("message", "Tipo de vaga inválido.");
                    return ResponseEntity.badRequest().body(response);
                }
            }
        if (updates.containsKey("fkEmpresaId")) {
                try {
                    vaga.setFkEmpresaId(((UUID) updates.get("fkEmpresaId")));
                } catch (ClassCastException e) {
                    response.put("message", "Empresa inválida");
                    return ResponseEntity.badRequest().body(response);
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
        response.put("message", "ok");
        return ResponseEntity.ok(response);
    }
}
