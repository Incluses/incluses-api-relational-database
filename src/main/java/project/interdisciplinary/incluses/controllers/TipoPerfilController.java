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
import project.interdisciplinary.incluses.models.TipoPerfil;
import project.interdisciplinary.incluses.services.TipoPerfilService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/tipo-perfil")
public class TipoPerfilController {
    private final TipoPerfilService tipoPerfilService;

    @Autowired
    public TipoPerfilController(TipoPerfilService tipoPerfilService) {
        this.tipoPerfilService = tipoPerfilService;
    }

    @Operation(summary = "Listar todos os tipos de perfil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de perfil retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<TipoPerfil> listarTipoPerfis() {
        return tipoPerfilService.listarTiposPerfis();
    }

    @Operation(summary = "Inserir um novo tipo de perfil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de perfil inserido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no tipo de perfil"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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
                response.put("message", "ok");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @Operation(summary = "Atualizar um tipo de perfil pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de perfil atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no tipo de perfil"),
            @ApiResponse(responseCode = "404", description = "Tipo de perfil não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }
}
