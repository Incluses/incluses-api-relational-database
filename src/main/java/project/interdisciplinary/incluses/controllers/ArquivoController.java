package project.interdisciplinary.incluses.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Arquivo;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.services.ArquivoService;

import java.util.*;

@RestController
@RequestMapping("/arquivo")
public class ArquivoController {
    private final ArquivoService arquivoService;
    @Autowired
    private Validator validator;

    public ArquivoController(ArquivoService arquivoService) {
        this.arquivoService = arquivoService;
    }

    @GetMapping("/selecionar")
    @Operation(summary = "Lista todos os arquivos", description = "Retorna uma lista de todos os Arquivos disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Arquivos retornados com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Arquivo.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public List<Arquivo> listarArquivos() {
        return arquivoService.listarArquivos();
    }

    @GetMapping("/selecionar-id/{id}")
    @Operation(summary = "Lista arquivo por id", description = "Retorna arquivo por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo retornado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Arquivo.class))),
            @ApiResponse(responseCode = "404", description = "Arquivo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Arquivo listarArquivoPorId(@PathVariable UUID id) {
        Arquivo arquivo = arquivoService.buscarArquivoPorId(id);
        return arquivo != null ? arquivo : null;
    }

    @PostMapping("/inserir")
    @Operation(summary = "Insere um arquivo", description = "Insere um arquivo de acordo com o schema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo inserido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Arquivo.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Object> inserirArquivo(@Valid @RequestBody Arquivo arquivo, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Arquivo arquivo1 = arquivoService.salvarArquivo(arquivo);
            return arquivo1.getId() == arquivo.getId() ? ResponseEntity.ok(arquivo1) : ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    @Operation(summary = "Atualiza parcialmente um arquivo", description = "Atualiza parcialmente um arquivo de acordo com o seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Arquivo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Object> atualizarArquivoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Arquivo arquivo = arquivoService.buscarArquivoPorId(id);
        Map<String, String> response = new HashMap<>();

        if (arquivo == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("nome")) {
            arquivo.setNome((String) updates.get("nome"));
        }
        if (updates.containsKey("s3Url")) {
            arquivo.setS3Url((String) updates.get("s3Url"));
        }
        if (updates.containsKey("s3Key")) {
            arquivo.setS3Key((String) updates.get("s3Key"));
        }
        if (updates.containsKey("tamanho")) {
            arquivo.setTamanho((String) updates.get("tamanho"));
        }
        if (updates.containsKey("fkTipoArquivoId")) {
            try {
                arquivo.setFkTipoArquivoId(UUID.fromString((String) updates.get("fkTipoArquivoId")));
            } catch (ClassCastException | IllegalArgumentException e) {
                response.put("message", "Tipo de arquivo inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        Set<ConstraintViolation<Arquivo>> violations = validator.validate(arquivo);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Arquivo> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        arquivoService.salvarArquivo(arquivo);
        response.put("message", "ok");
        return ResponseEntity.ok(response);
    }
}
