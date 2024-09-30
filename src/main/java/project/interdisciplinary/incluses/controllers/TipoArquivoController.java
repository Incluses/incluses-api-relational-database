package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.TipoArquivo;
import project.interdisciplinary.incluses.services.TipoArquivoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tipo-arquivo")
public class TipoArquivoController {
    private final TipoArquivoService tipoArquivoService;

    @Autowired
    public TipoArquivoController(TipoArquivoService tipoArquivoService) {
        this.tipoArquivoService = tipoArquivoService;
    }

    @GetMapping("/selecionar")
    public List<TipoArquivo> listarTipoArquivos() {
        return tipoArquivoService.listarTipoArquivos();
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirTipoArquivo(@Valid @RequestBody TipoArquivo tipoArquivo, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            TipoArquivo tipoArquivo1 = tipoArquivoService.salvarTipoArquivo(tipoArquivo);
            if (tipoArquivo1.getId() == tipoArquivo.getId()) {
                return ResponseEntity.ok("Inserido com sucesso");
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirTipoArquivo(@PathVariable UUID id) {
        if (tipoArquivoService.excluirTipoArquivo(id) != null) {
            return ResponseEntity.ok("Tipo de arquivo excluído com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarTipoArquivo(@PathVariable UUID id, @Valid @RequestBody TipoArquivo tipoArquivoAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            TipoArquivo tipoArquivo = tipoArquivoService.buscarTipoArquivoPorId(id);
            tipoArquivo.setNome(tipoArquivoAtualizado.getNome());
            tipoArquivoService.salvarTipoArquivo(tipoArquivo);
            return ResponseEntity.ok("Tipo de arquivo atualizado com sucesso");
        }
    }
}
