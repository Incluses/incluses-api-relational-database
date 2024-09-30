package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Usuario;
import project.interdisciplinary.incluses.services.UsuarioService;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final Validator validator;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, Validator validator) {
        this.usuarioService = usuarioService;
        this.validator = validator;
    }

    @GetMapping("/selecionar")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirUsuario(@Valid @RequestBody Usuario usuario, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Usuario usuario1 = usuarioService.salvarUsuario(usuario);
            if (usuario1.getId() == usuario.getId()) {
                return ResponseEntity.ok("Inserido com sucesso");
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirUsuario(@PathVariable UUID id) {
        if (usuarioService.excluirUsuario(id) != null) {
            return ResponseEntity.ok("Usuário excluído com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarUsuario(@PathVariable UUID id, @Valid @RequestBody Usuario usuarioAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Usuario usuario = usuarioService.buscarUsuarioPorId(id);
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }
            usuario.setCpf(usuarioAtualizado.getCpf());
            usuario.setFkPerfilId(usuarioAtualizado.getFkPerfilId());
            usuario.setDtNascimento(usuarioAtualizado.getDtNascimento());
            usuarioService.salvarUsuario(usuario);
            return ResponseEntity.ok("Usuário atualizado com sucesso");
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarUsuarioParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("cpf")) {
            usuario.setCpf((String) updates.get("cpf"));
        }
        if (updates.containsKey("fkPerfilId")) {
            try {
                usuario.setFkPerfilId(((UUID) updates.get("fkPerfilId")));
            } catch (ClassCastException e) {
                return ResponseEntity.badRequest().body("Perfil inválido.");
            }
        }
        if (updates.containsKey("dtNascimento")) {
            usuario.setDtNascimento((Date) updates.get("dtNascimento"));
        }

        // Validate the updated Usuario object
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Usuario> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok("Usuário atualizado com sucesso");
    }
}

