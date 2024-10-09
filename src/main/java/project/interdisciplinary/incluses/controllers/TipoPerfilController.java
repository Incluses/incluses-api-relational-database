package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.TipoPerfil;
import project.interdisciplinary.incluses.services.TipoPerfilService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tipo-perfil")
public class TipoPerfilController {
    private final TipoPerfilService tipoPerfilService;

    @Autowired
    public TipoPerfilController(TipoPerfilService tipoPerfilService) {
        this.tipoPerfilService = tipoPerfilService;
    }

    @GetMapping("/selecionar")
    public List<TipoPerfil> listarTipoPerfis() {
        return tipoPerfilService.listarTiposPerfis();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirTipoPerfil(@Valid @RequestBody TipoPerfil tipoPerfil, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            TipoPerfil tipoPerfil1 = tipoPerfilService.salvarTipoPerfil(tipoPerfil);
            if (tipoPerfil1.getId() == tipoPerfil.getId()) {
                Map<String, String> response = new HashMap<>();
                response.put("message","ok");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirTipoPerfil(@PathVariable UUID id) {
        if (tipoPerfilService.excluirTipoPerfil(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message","ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarTipoPerfil(@PathVariable UUID id, @Valid @RequestBody TipoPerfil tipoPerfilAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            TipoPerfil tipoPerfil = tipoPerfilService.buscarTipoPerfilPorId(id);
            tipoPerfil.setNome(tipoPerfilAtualizado.getNome());
            tipoPerfilService.salvarTipoPerfil(tipoPerfil);
            Map<String, String> response = new HashMap<>();
            response.put("message","ok");
            return ResponseEntity.ok(response);
        }
    }
}
