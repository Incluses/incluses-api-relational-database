package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Empresa;
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

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirEmpresa(@Valid @RequestBody Empresa empresa, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Empresa empresa1 = empresaService.salvarEmpresa(empresa);
            if (empresa1.getId() == empresa.getId()) {
                return ResponseEntity.ok("Inserida com sucesso");
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirEmpresa(@PathVariable UUID id) {
        if (empresaService.excluirEmpresa(id) != null) {
            return ResponseEntity.ok("Empresa excluída com sucesso");
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
            return ResponseEntity.ok("Empresa atualizada com sucesso");
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarEmpresaParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        Empresa empresa = empresaService.buscarEmpresaPorId(id);

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
                empresa.setFkPerfilId(((UUID) updates.get("fkPerfilId")));
            } catch (ClassCastException e) {
                return ResponseEntity.badRequest().body("Perfil inválido.");
            }
        }
        if (updates.containsKey("website")) {
            empresa.setWebsite((String) updates.get("website"));
        }
        if (updates.containsKey("fkEnderecoId")) {
            try {
                empresa.setFkEnderecoId(((UUID) updates.get("fkEnderecoId")));
            } catch (ClassCastException e) {
                return ResponseEntity.badRequest().body("Endereço inválido.");
            }
        }

        // Validate the updated Empresa object
        Set<ConstraintViolation<Empresa>> violations = validator.validate(empresa);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<Empresa> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        empresaService.salvarEmpresa(empresa);
        return ResponseEntity.ok("Empresa atualizada com sucesso");
    }
}
