package project.interdisciplinary.incluses.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.MaterialCurso;
import project.interdisciplinary.incluses.models.Perfil;
import project.interdisciplinary.incluses.models.dto.CriarMaterialCursoDTO;
import project.interdisciplinary.incluses.services.MaterialCursoService;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/material-curso")
public class MaterialCursoController {
    private final MaterialCursoService materialCursoService;
    private final Validator validator;

    @Autowired
    public MaterialCursoController(MaterialCursoService materialCursoService, Validator validator) {
        this.materialCursoService = materialCursoService;
        this.validator = validator;
    }

    @GetMapping("/selecionar")
    public List<MaterialCurso> listarMateriaisCursos() {
        return materialCursoService.listarMateriaisCursos();
    }

    @GetMapping("/selecionar-fk-curso/{fkCurso}")
    public List<MaterialCurso> buscarMaterialDoCurso (@PathVariable UUID fkCurso){
        List<MaterialCurso> materialCursos = materialCursoService.findMaterialByFkCurso(fkCurso);
        if (materialCursos != null){
            return materialCursos;
        }
        else {
            return null;
        }
    }
    @GetMapping("/selecionar-nome/{nome}/{fkCurso}")
    public List<MaterialCurso> buscarMaterialPorNome(@PathVariable("nome") String nome,@PathVariable("fkCurso") UUID fkCurso){
        List<MaterialCurso> materialCursos = materialCursoService.findMaterialByNome(fkCurso, nome);
        if (materialCursos != null){
            return materialCursos;
        }
        else {
            return null;
        }
    }

    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirMaterialCurso(@Valid @RequestBody CriarMaterialCursoDTO materialCurso, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            materialCursoService.criarMaterialCurso(materialCurso);
            Map<String, String> response = new HashMap<>();
            response.put("message","ok");
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirMaterialCurso(@PathVariable UUID id) {
        if (materialCursoService.excluirMaterialCurso(id) == true) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarMaterialCurso(@PathVariable UUID id, @Valid @RequestBody MaterialCurso materialCursoAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            MaterialCurso materialCurso = materialCursoService.buscarMaterialCursoPorId(id);
            if (materialCurso == null) {
                return ResponseEntity.notFound().build();
            }
            materialCurso.setNome(materialCursoAtualizado.getNome());
            materialCurso.setFkCursoId(materialCursoAtualizado.getFkCursoId());
            materialCurso.setFkArquivoId(materialCursoAtualizado.getFkArquivoId());
            materialCurso.setDescricao(materialCursoAtualizado.getDescricao());
            materialCursoService.salvarMaterialCurso(materialCurso);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<Object> atualizarMaterialCursoParcial(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        MaterialCurso materialCurso = materialCursoService.buscarMaterialCursoPorId(id);
        Map<String, String> response = new HashMap<>();

        if (materialCurso == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.containsKey("nome")) {
            materialCurso.setNome((String) updates.get("nome"));
        }
        if (updates.containsKey("fkCursoId")) {
            try {
                materialCurso.setFkCursoId(UUID.fromString((String) updates.get("fkCursoId")));
            } catch (ClassCastException e) {
                response.put("message", "Curso inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("fkArquivoId")) {
            try {
                materialCurso.setFkArquivoId(UUID.fromString((String) updates.get("fkArquivoId")));
            } catch (ClassCastException e) {
                response.put("message", "Arquivo inválido.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        if (updates.containsKey("descricao")) {
            materialCurso.setDescricao((String) updates.get("descricao"));
        }

        // Validate the updated MaterialCurso object
        Set<ConstraintViolation<MaterialCurso>> violations = validator.validate(materialCurso);
        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<MaterialCurso> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        materialCursoService.salvarMaterialCurso(materialCurso);
        response.put("message", "ok");
        return ResponseEntity.ok(response);
    }
}
