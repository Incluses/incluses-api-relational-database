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

    @GetMapping("/selecionar-id/{id}")
    public Perfil listarPerfilPorId(@PathVariable UUID id){
        Perfil perfil = perfilService.buscarPerfilPorId(id);
        if (perfil != null){
            return perfil;
        }
        else {
            return null;
        }
    }

    @GetMapping("/selecionar-email/{email}")
    public Perfil buscarPerfil (@PathVariable String email){
        Perfil perfil = perfilService.findByEmail(email);
        if (perfil != null){
            return perfil;
        }
        else {
            return null;
        }
    }
    @GetMapping("/selecionar-nome/{nome}")
    public List<Perfil> buscarPerfilNome (@PathVariable String nome){
        List<Perfil> perfils = perfilService.findByNome(nome);
        if (perfils != null){
            return perfils;
        }
        else {
            return null;
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
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);        }
    }


    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarPerfilParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Perfil perfil = perfilService.buscarPerfilPorId(id);
        Map<String, String> response = new HashMap<>();

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
                perfil.setFkTipoPerfilId(UUID.fromString((String) updates.get("fkTipoPerfilId")));
            } catch (ClassCastException e) {
                response.put("message", "Tipo de perfil inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("fkFtPerfilId")) {
            try {
                perfil.setFkFtPerfilId(UUID.fromString((String) updates.get("fkFtPerfilId")));
            } catch (ClassCastException e) {
                response.put("message", "Foto de perfil inválida.");
                return ResponseEntity.badRequest().body(response);
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

        Perfil perfil2 = perfilService.salvarPerfil(perfil); // Supondo que você tenha um método para salvar o Perfil
        return ResponseEntity.ok(perfil2);
    }

}

