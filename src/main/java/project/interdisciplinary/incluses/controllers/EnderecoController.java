package project.interdisciplinary.incluses.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Endereco;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.services.EnderecoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.*;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {
    private final EnderecoService enderecoService;

    @Autowired
    private Validator validator;

    @Autowired
    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @Operation(summary = "Listar todos os endereços")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<Endereco> listarEnderecos() {
        return enderecoService.listarEnderecos();
    }

    @Operation(summary = "Inserir um novo endereço")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirEndereco(@Valid @RequestBody Endereco endereco, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Endereco endereco1 = enderecoService.salvarEndereco(endereco);
            if (endereco1.getId() == endereco.getId()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "ok");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @Operation(summary = "Atualizar parcialmente um endereço")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
    ),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarEnderecoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Endereco endereco = enderecoService.buscarEnderecoPorId(id);
        Map<String, String> response = new HashMap<>();

        if (endereco == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("rua")) {
            endereco.setRua((String) updates.get("rua"));
        }
        if (updates.containsKey("estado")) {
            endereco.setEstado((String) updates.get("estado"));
        }
        if (updates.containsKey("numero")) {
            try {
                endereco.setNumero(((Number) updates.get("numero")).intValue());
            } catch (ClassCastException e) {
                response.put("message", "Número inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        // Validate the updated Endereco object
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Endereco> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        enderecoService.salvarEndereco(endereco);
        response.put("message", "ok");
        return ResponseEntity.ok(response);
    }
}
