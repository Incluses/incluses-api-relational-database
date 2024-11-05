package project.interdisciplinary.incluses.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.models.TipoArquivo;
import project.interdisciplinary.incluses.services.TipoArquivoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/tipo-arquivo")
public class TipoArquivoController {
    private final TipoArquivoService tipoArquivoService;

    @Autowired
    public TipoArquivoController(TipoArquivoService tipoArquivoService) {
        this.tipoArquivoService = tipoArquivoService;
    }

    @Operation(summary = "Listar todos os tipos de arquivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de arquivos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<TipoArquivo> listarTipoArquivos() {
        return tipoArquivoService.listarTipoArquivos();
    }

    @Operation(summary = "Buscar tipo de arquivo pelo nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de arquivo encontrado ou criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Tipo de arquivo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar-nome/{nome}")
    public TipoArquivo buscarTipoPorNome(@PathVariable String nome) {
        TipoArquivo tipo = tipoArquivoService.findByNome(nome);
        if (tipo != null) {
            return tipo;
        } else {
            TipoArquivo tipoArquivo = new TipoArquivo();
            tipoArquivo.setNome(nome);
            return tipoArquivoService.salvarTipoArquivo(tipoArquivo);
        }
    }

    @Operation(summary = "Inserir um novo tipo de arquivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de arquivo inserido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no tipo de arquivo"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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
                Map<String, String> response = new HashMap<>();
                response.put("message", "ok");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @Operation(summary = "Excluir um tipo de arquivo pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de arquivo excluído com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Tipo de arquivo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirTipoArquivo(@PathVariable UUID id) {
        if (tipoArquivoService.excluirTipoArquivo(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar um tipo de arquivo pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de arquivo atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no tipo de arquivo"),
            @ApiResponse(responseCode = "404", description = "Tipo de arquivo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }
}
