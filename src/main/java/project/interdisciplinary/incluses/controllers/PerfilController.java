package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Perfil;
import project.interdisciplinary.incluses.services.PerfilService;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/perfil")
public class PerfilController {
    private final PerfilService perfilService;
    private final Validator validator;

    @Autowired
    public PerfilController(PerfilService perfilService, Validator validator) {
        this.perfilService = perfilService;
        this.validator = validator;
    }

    @GetMapping("/selecionar")
    public List<Perfil> listarPerfis() {
        return perfilService.listarPerfis();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirPerfil(@Valid @RequestBody Perfil perfil, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Perfil perfil1 = perfilService.salvarPerfil(perfil);
            if (perfil1.getId() == perfil.getId()) {
                return ResponseEntity.ok("Inserido com sucesso");
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirPerfil(@PathVariable UUID id) {
        if (perfilService.excluirPerfil(id) != null) {
            return ResponseEntity.ok("Perfil excluído com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarPerfil(@PathVariable UUID id, @Valid @RequestBody Perfil perfilAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Perfil perfil = perfilService.buscarPerfilPorId(id);
            perfil.setNome(perfilAtualizado.getNome());
            perfil.setSenha(perfilAtualizado.getSenha());
            perfil.setEmail(perfilAtualizado.getEmail());
            perfil.setBiografia(perfilAtualizado.getBiografia());
            perfil.setFkTipoPerfilId(perfilAtualizado.getFkTipoPerfilId());
            perfilService.salvarPerfil(perfil);
            return ResponseEntity.ok("Perfil atualizado com sucesso");
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarPerfilParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Perfil perfil = perfilService.buscarPerfilPorId(id);

        if (perfil == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("nome")) {
            perfil.setNome((String) updates.get("nome"));
        }
        if (updates.containsKey("senha")) {
            perfil.setSenha((String) updates.get("senha"));
        }
        if (updates.containsKey("email")) {
            perfil.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("biografia")) {
            perfil.setBiografia((String) updates.get("biografia"));
        }
        if (updates.containsKey("fkTipoPerfilId")) {
            try {
                perfil.setFkTipoPerfilId(((UUID) updates.get("fkTipoPerfilId")));
            } catch (ClassCastException e) {
                return ResponseEntity.badRequest().body("Tipo de perfil inválido.");
            }
        }

        // Validate the updated Perfil object
        Set<ConstraintViolation<Perfil>> violations = validator.validate(perfil);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Perfil> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        perfilService.salvarPerfil(perfil);
        return ResponseEntity.ok("Perfil atualizado com sucesso");
    }
}

