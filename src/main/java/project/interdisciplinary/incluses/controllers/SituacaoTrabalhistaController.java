package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.SituacaoTrabalhista;
import project.interdisciplinary.incluses.services.SituacaoTrabalhistaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/situacao-trabalhista")
public class SituacaoTrabalhistaController {
    private final SituacaoTrabalhistaService situacaoTrabalhistaService;

    @Autowired
    public SituacaoTrabalhistaController(SituacaoTrabalhistaService situacaoTrabalhistaService) {
        this.situacaoTrabalhistaService = situacaoTrabalhistaService;
    }

    @GetMapping("/selecionar")
    public List<SituacaoTrabalhista> listarSituacoesTrabalhistas() {
        return situacaoTrabalhistaService.listarSituacoesTrabalhistas();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirSituacaoTrabalhista(@Valid @RequestBody SituacaoTrabalhista situacaoTrabalhista, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            SituacaoTrabalhista situacaoTrabalhista1 = situacaoTrabalhistaService.salvarSituacaoTrabalhista(situacaoTrabalhista);
            if (situacaoTrabalhista1.getId() == situacaoTrabalhista.getId()) {
                Map<String, String> response = new HashMap<>();
                response.put("message","ok");
                return ResponseEntity.ok(response);
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirSituacaoTrabalhista(@PathVariable UUID id) {
        if (situacaoTrabalhistaService.excluirSituacaoTrabalhista(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message","ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarSituacaoTrabalhista(@PathVariable UUID id, @Valid @RequestBody SituacaoTrabalhista situacaoTrabalhistaAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            SituacaoTrabalhista situacaoTrabalhista = situacaoTrabalhistaService.buscarSituacaoTrabalhistaPorId(id);
            situacaoTrabalhista.setNome(situacaoTrabalhistaAtualizado.getNome());
            situacaoTrabalhistaService.salvarSituacaoTrabalhista(situacaoTrabalhista);
            Map<String, String> response = new HashMap<>();
            response.put("message","ok");
            return ResponseEntity.ok(response);        }
    }
}
