package project.interdisciplinary.incluses.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.models.Empresa;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.models.dto.CriarEmpresaDTO;
import project.interdisciplinary.incluses.services.EmpresaService;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {
    private final EmpresaService empresaService;
    private final Validator validator;

    @Autowired
    public EmpresaController(EmpresaService empresaService, Validator validator) {
        this.empresaService = empresaService;
        this.validator = validator;
    }

    @Operation(summary = "Lista todas as empresas", description = "Retorna uma lista de todas as empresas disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empresas retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empresa.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<Empresa> listarEmpresas() {
        return empresaService.listarEmpresas();
    }

    @Operation(summary = "Busca empresa por perfil", description = "Retorna a empresa associada ao perfil fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empresa.class))),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    @GetMapping("/selecionar-fk-perfil/{fkPerfil}")
    public Object acharEmpresaPorFkPerfil(@PathVariable UUID fkPerfil) {
        Empresa empresa = empresaService.acharPorFkPerfil(fkPerfil);
        if (empresa != null) {
            return empresa;
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "nada encontrado");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Insere uma nova empresa", description = "Insere uma nova empresa com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa inserida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados da empresa")
    })
    @PostMapping("/public/inserir")
    public ResponseEntity<Object> inserirEmpresa(@Valid @RequestBody CriarEmpresaDTO empresa, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            empresaService.criarEmpresa(empresa);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Exclui uma empresa", description = "Exclui a empresa com o ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa excluída com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirEmpresa(@PathVariable UUID id) {
        if (empresaService.excluirEmpresa(id)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualiza parcialmente uma empresa", description = "Atualiza parcialmente os dados de uma empresa com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empresa.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados atualizados"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarEmpresaParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Empresa empresa = empresaService.buscarEmpresaPorId(id);
        Map<String, String> response = new HashMap<>();

        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("cnpj")) {
            empresa.setCnpj((String) updates.get("cnpj"));
        }
        if (updates.containsKey("razaoSocial")) {
            empresa.setRazaoSocial((String) updates.get("razaoSocial"));
        }
        if (updates.containsKey("fkPerfilId")) {
            try {
                empresa.setFkPerfilId(UUID.fromString((String) updates.get("fkPerfilId")));
            } catch (ClassCastException | IllegalArgumentException e) {
                response.put("message", "Perfil inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("website")) {
            empresa.setWebsite((String) updates.get("website"));
        }
        if (updates.containsKey("fkEnderecoId")) {
            try {
                empresa.setFkEnderecoId(UUID.fromString((String) updates.get("fkEnderecoId")));
            } catch (ClassCastException | IllegalArgumentException e) {
                response.put("message", "Endereço inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("fkSetorId")) {
            try {
                empresa.setFkSetorId(UUID.fromString((String) updates.get("fkSetorId")));
            } catch (ClassCastException | IllegalArgumentException e) {
                response.put("message", "Setor inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        Set<ConstraintViolation<Empresa>> violations = validator.validate(empresa);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Empresa> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        Empresa empresa1 = empresaService.salvarEmpresa(empresa);
        return ResponseEntity.ok(empresa1);
    }
}
