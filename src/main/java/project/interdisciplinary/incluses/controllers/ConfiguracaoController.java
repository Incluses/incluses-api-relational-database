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
import project.interdisciplinary.incluses.models.Message;
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

    @Operation(summary = "Lista todas as configurações", description = "Retorna uma lista de todas as Configurações disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Configurações retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Configuracao.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<Configuracao> listarConfiguracoes() {
        return configuracaoService.listarConfiguracoes();
    }

    @Operation(summary = "Atualiza configuração parcialmente", description = "Atualiza uma configuração parcialmente com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuração atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou Perfil inválido",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Configuração não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/atualizarParcial/{id}")
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
        return ResponseEntity.ok(response);
    }
}
