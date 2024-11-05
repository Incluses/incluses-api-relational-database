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
import project.interdisciplinary.incluses.models.TipoVaga;
import project.interdisciplinary.incluses.services.TipoVagaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/tipo-vaga")
public class TipoVagaController {
    private final TipoVagaService tipoVagaService;

    @Autowired
    public TipoVagaController(TipoVagaService tipoVagaService) {
        this.tipoVagaService = tipoVagaService;
    }

    @Operation(summary = "Listar todos os tipos de vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de vaga retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<TipoVaga> listarTipoVagas() {
        return tipoVagaService.listarTiposVaga();
    }

    @Operation(summary = "Inserir um novo tipo de vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de vaga inserido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no tipo de vaga"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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
                response.put("message", "ok");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }
    @Operation(summary = "Atualizar um tipo de vaga pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de vaga atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no tipo de vaga"),
            @ApiResponse(responseCode = "404", description = "Tipo de vaga não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }
}
