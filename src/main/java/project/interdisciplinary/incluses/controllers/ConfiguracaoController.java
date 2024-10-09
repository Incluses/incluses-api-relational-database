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
import project.interdisciplinary.incluses.models.Configuracao;
import project.interdisciplinary.incluses.services.ConfiguracaoService;

import java.util.*;

@RestController
@RequestMapping("/configuracao")
public class ConfiguracaoController {
    private final ConfiguracaoService configuracaoService;
    @Autowired
    private Validator validator;

    public ConfiguracaoController(ConfiguracaoService configuracaoService) {
        this.configuracaoService = configuracaoService;
    }

    @GetMapping("/selecionar")
    @Operation(summary = "Lista todas as configurações",
            description = "Retorna uma lista de todas as Configurações disponiveis")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Lista de Configurações retornados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Arquivo.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    public List<Configuracao> listarConfiguracoes() {
        return configuracaoService.listarConfiguracoes();
    }


    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualizar uma configuração",
            description = "Atualizar uma configuração de acordo com o seu id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Configuração atualizada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Arquivo.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    public ResponseEntity<Object> atualizarConfiguracao(@PathVariable UUID id, @Valid
    @RequestBody Configuracao configuracaoAtualizada, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Configuracao configuracao = configuracaoService.buscarConfiguracaoPorId(id);
            if (configuracao == null) {
                return ResponseEntity.notFound().build();
            }
            configuracao.setNotificacao(configuracaoAtualizada.getNotificacao());
            configuracao.setFkPerfilId(configuracaoAtualizada.getFkPerfilId());
            configuracaoService.salvarConfiguracao(configuracao);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    @Operation(summary = "Atualizar configuração parcialmente",
            description = "Atualizar configuração parcialmente de acordo com o seu id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Configuração atualizada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Arquivo.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    public ResponseEntity<Object> atualizarConfiguracaoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Configuracao configuracao = configuracaoService.buscarConfiguracaoPorId(id);
        Map<String, String> response = new HashMap<>();

        if (configuracao == null) {
            return ResponseEntity.notFound().build();
        }
        if (updates.containsKey("notificacao")) {
            configuracao.setNotificacao((Boolean) updates.get("notificacao"));
        }
        if (updates.containsKey("fkPerfilId")) {
            try {
                configuracao.setFkPerfilId((UUID) updates.get("fkPerfilId"));
            } catch (ClassCastException e) {
                response.put("message", "Perfil inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        // Validate the updated Configuracao object
        Set<ConstraintViolation<Configuracao>> violations = validator.validate(configuracao);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Configuracao> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessageTemplate());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        configuracaoService.salvarConfiguracao(configuracao);
        response.put("message", "ok");
        return ResponseEntity.ok(response);    }
}
