package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.TipoVaga;
import project.interdisciplinary.incluses.services.TipoVagaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tipo-vaga")
public class TipoVagaController {
    private final TipoVagaService tipoVagaService;

    @Autowired
    public TipoVagaController(TipoVagaService tipoVagaService) {
        this.tipoVagaService = tipoVagaService;
    }

    @GetMapping("/selecionar")
    public List<TipoVaga> listarTipoVagas() {
        return tipoVagaService.listarTiposVaga();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirTipoVaga(@Valid @RequestBody TipoVaga tipoVaga, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            TipoVaga tipoVaga1 = tipoVagaService.salvarTipoVaga(tipoVaga);
            if (tipoVaga1.getId() == tipoVaga.getId()) {
                Map<String, String> response = new HashMap<>();
                response.put("message","ok");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirTipoVaga(@PathVariable UUID id) {
        if (tipoVagaService.excluirTipoVaga(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message","ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarTipoVaga(@PathVariable UUID id, @Valid @RequestBody TipoVaga tipoVagaAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            TipoVaga tipoVaga = tipoVagaService.buscarTipoVagaPorId(id);
            tipoVaga.setNome(tipoVagaAtualizado.getNome());
            tipoVagaService.salvarTipoVaga(tipoVaga);
            Map<String, String> response = new HashMap<>();
            response.put("message","ok");
            return ResponseEntity.ok(response);
        }
    }
}