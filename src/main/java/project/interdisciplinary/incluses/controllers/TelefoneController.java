package project.interdisciplinary.incluses.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Telefone;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.services.TelefoneService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/telefone")
public class TelefoneController {

    private final TelefoneService telefoneService;

    @Autowired
    public TelefoneController(TelefoneService telefoneService) {
        this.telefoneService = telefoneService;
    }

    @Operation(summary = "Listar todos os telefones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de telefones retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<Telefone> listarTelefones() {
        return telefoneService.listarTelefones();
    }

    @Operation(summary = "Inserir um novo telefone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telefone inserido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no telefone"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirTelefone(@Valid @RequestBody Telefone telefone, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Telefone telefoneSalvo = telefoneService.salvarTelefone(telefone);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Atualizar um telefone pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telefone atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no telefone"),
            @ApiResponse(responseCode = "404", description = "Telefone não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarTelefone(@PathVariable UUID id, @Valid @RequestBody Telefone telefoneAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Telefone telefone = telefoneService.buscarTelefonePorId(id);
            if (telefone == null) {
                return ResponseEntity.notFound().build();
            }
            telefone.setTelefone(telefoneAtualizado.getTelefone());
            telefoneService.salvarTelefone(telefone);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }
}
