package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Setor;
import project.interdisciplinary.incluses.services.SetorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/setor")
public class SetorController {
    private final SetorService setorService;

    @Autowired
    public SetorController(SetorService setorService) {
        this.setorService = setorService;
    }

    @GetMapping("/selecionar")
    public List<Setor> listarSetores() {
        return setorService.listarSetores();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirSetor(@Valid @RequestBody Setor setor, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Setor setor1 = setorService.salvarSetor(setor);
            if (setor1.getId() == setor.getId()) {
                return ResponseEntity.ok("Inserido com sucesso");
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirSetor(@PathVariable UUID id) {
        if (setorService.excluirSetor(id) != null) {
            return ResponseEntity.ok("Setor excluído com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarSetor(@PathVariable UUID id, @Valid @RequestBody Setor setorAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Setor setor = setorService.buscarSetorPorId(id);
            setor.setNome(setorAtualizado.getNome());
            setorService.salvarSetor(setor);
            return ResponseEntity.ok("Setor atualizado com sucesso");
        }
    }
}
