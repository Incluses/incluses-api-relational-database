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
import project.interdisciplinary.incluses.models.Perfil;
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
    @Operation(summary = "Lista todos os arquivos",
            description = "Retorna uma lista de todos os Arquivos disponiveis")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Lista de Arquivos retornados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Arquivo.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    public List<Arquivo> listarArquivos() {
        return arquivoService.listarArquivos();
    }

    @GetMapping("/selecionar-id/{id}")
    public Arquivo listarArquivoPorId(@PathVariable UUID id){
        Arquivo arquivo = arquivoService.buscarArquivoPorId(id);
        if (arquivo != null){
            return arquivo;
        }
        else {
            return null;
        }
    }

    @PostMapping("/inserir")
    @Operation(summary = "Insere um arquivo",
            description = "Insere um arquivo de acordo com o schema")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Arquivo inserido com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Arquivo.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor"),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Arquivo.class)))})
    public ResponseEntity<Object> inserirArquivo(@Valid @RequestBody Arquivo arquivo,
                                                 BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Arquivo arquivo1 = arquivoService.salvarArquivo(arquivo);
            if (arquivo1.getId() == arquivo.getId()) {
                return ResponseEntity.ok(arquivo1);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    @Operation(summary = "Deleta um arquivo",
            description = "Deleta um arquivo de acordo com o seu id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Arquivo excluido com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Arquivo.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor"),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Arquivo.class)))})
    public ResponseEntity<Object> excluirArquivo(@PathVariable UUID id) {
        if (arquivoService.excluirArquivo(id) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualizar um arquivo",
            description = "Atualizar um arquivo de acordo com o seu id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Arquivo atualizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Arquivo.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    public ResponseEntity<Object> atualizarArquivo(@PathVariable UUID id, @Valid
    @RequestBody Arquivo arquivoAtualizado, BindingResult resultado) {
        Arquivo arquivo;
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            arquivo = arquivoService.buscarArquivoPorId(id);
            arquivo.setNome(arquivoAtualizado.getNome());
            arquivo.setTamanho(arquivoAtualizado.getTamanho());
            arquivo.setS3Key(arquivoAtualizado.getS3Key());
            arquivo.setS3Url(arquivoAtualizado.getS3Url());
            arquivo.setFkTipoArquivoId(arquivoAtualizado.getFkTipoArquivoId());
            arquivoService.salvarArquivo(arquivo);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    @Operation(summary = "Atualizar um arquivo parcialmente",
            description = "Atualizar um arquivo parcialmente de acordo com o seu id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Arquivo atualizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Arquivo.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    public ResponseEntity atualizarArquivoParcial(@PathVariable UUID id, @RequestBody Map updates) {
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
                arquivo.setFkTipoArquivoId(((UUID) updates.get("fkTipoArquivoId")));
            } catch (ClassCastException e) {
                response.put("message", "Tipo de arquivo inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        // Validate the updated Produto object
        Set<ConstraintViolation<Arquivo>> violations = validator.validate(arquivo);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Arquivo> violationsIn : violations) {
                errors.put(violationsIn.getPropertyPath().toString(), violationsIn.getMessageTemplate());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        arquivoService.salvarArquivo(arquivo);
        response.put("message", "ok");
        return ResponseEntity.ok(response);
    }
}