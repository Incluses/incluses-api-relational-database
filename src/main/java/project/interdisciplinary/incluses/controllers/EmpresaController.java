package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Empresa;
import project.interdisciplinary.incluses.models.Usuario;
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

    @GetMapping("/selecionar")
    public List<Empresa> listarEmpresas() {
        return empresaService.listarEmpresas();
    }

    @GetMapping("/selecionar-fk-perfil/{fkPerfil}")
    public Object acharUsuarioPorFkPerfil(@PathVariable UUID fkPerfil){
        Empresa empresa = empresaService.acharPorFkPerfil(fkPerfil);
        if(empresa != null){
            return empresa;
        }
        else {
            Map<String, String> response = new HashMap<>();
            response.put("message","nada encontrado");
            return ResponseEntity.ok(response);
        }
    }
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

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirEmpresa(@PathVariable UUID id) {
        if (empresaService.excluirEmpresa(id) == true) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarEmpresa(@PathVariable UUID id, @Valid @RequestBody Empresa empresaAtualizada, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Empresa empresa = empresaService.buscarEmpresaPorId(id);
            if (empresa == null) {
                return ResponseEntity.notFound().build();
            }
            empresa.setCnpj(empresaAtualizada.getCnpj());
            empresa.setRazaoSocial(empresaAtualizada.getRazaoSocial());
            empresa.setFkPerfilId(empresaAtualizada.getFkPerfilId());
            empresa.setWebsite(empresaAtualizada.getWebsite());
            empresa.setFkEnderecoId(empresaAtualizada.getFkEnderecoId());
            empresaService.salvarEmpresa(empresa);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

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

        // Validação do objeto `Empresa` atualizado
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
