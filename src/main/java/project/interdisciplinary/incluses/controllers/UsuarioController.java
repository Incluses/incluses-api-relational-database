package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Usuario;
import project.interdisciplinary.incluses.models.dto.CriarUsuarioDTO;
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

    @GetMapping("/selecionar-fk-perfil/{fkPerfil}")
    public Object acharUsuarioPorFkPerfil(@PathVariable UUID fkPerfil){
        Usuario usuario = usuarioService.acharPorFkPerfil(fkPerfil);
        if(usuario != null){
            return usuario;
        }
        else {
            Map<String, String> response = new HashMap<>();
            response.put("message","nada encontrado");
            return ResponseEntity.ok(response);
        }
    }


    @PostMapping("/public/inserir")
    public ResponseEntity<Object> inserirUsuario(@Valid @RequestBody CriarUsuarioDTO usuario, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            usuarioService.registrarUsuario(usuario);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirUsuario(@PathVariable UUID id) {
        if (usuarioService.excluirUsuario(id) == true) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarUsuario(@PathVariable UUID id, @Valid @RequestBody Usuario usuarioAtualizado, BindingResult resultado) {
        Map<String, String> response = new HashMap<>();
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
            response.put("message", "ok");
            return ResponseEntity.ok(response);        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarUsuarioParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        Map<String, String> response = new HashMap<>();

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
                response.put("message","Perfil inválido.");
                return ResponseEntity.badRequest().body(response);
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
        response.put("message","ok");
        return ResponseEntity.ok(response);    }
}

