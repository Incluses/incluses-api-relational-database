package project.interdisciplinary.incluses.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Endereco;
import project.interdisciplinary.incluses.services.EnderecoService;

import java.util.*;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {
    private final EnderecoService enderecoService;
    @Autowired
    private Validator validator;

    @Autowired
    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping("/selecionar")
    public List<Endereco> listarEnderecos() {
        return enderecoService.listarEnderecos();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirEndereco(@Valid @RequestBody Endereco endereco, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Endereco endereco1 = enderecoService.salvarEndereco(endereco);
            if (endereco1.getId() == endereco.getId()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "ok");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirEndereco(@PathVariable UUID id) {
        if (enderecoService.excluirEndereco(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarEndereco(@PathVariable UUID id, @Valid @RequestBody Endereco enderecoAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Endereco endereco = enderecoService.buscarEnderecoPorId(id);
            endereco.setRua(enderecoAtualizado.getRua());
            endereco.setEstado(enderecoAtualizado.getEstado());
            endereco.setNumero(enderecoAtualizado.getNumero());
            enderecoService.salvarEndereco(endereco);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarEnderecoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Endereco endereco = enderecoService.buscarEnderecoPorId(id);
        Map<String, String> response = new HashMap<>();

        if (endereco == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("rua")) {
            endereco.setRua((String) updates.get("rua"));
        }
        if (updates.containsKey("estado")) {
            endereco.setEstado((String) updates.get("estado"));
        }
        if (updates.containsKey("numero")) {
            try {
                endereco.setNumero(((Number) updates.get("numero")).intValue());
            } catch (ClassCastException e) {
                response.put("message", "ok");
                return ResponseEntity.badRequest().body("Número inválido.");
            }
        }

        // Validate the updated Endereco object
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Endereco> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        enderecoService.salvarEndereco(endereco);
        response.put("message", "ok");
        return ResponseEntity.ok(response);
    }
}
