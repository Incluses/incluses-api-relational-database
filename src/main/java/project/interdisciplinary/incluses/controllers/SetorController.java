package project.interdisciplinary.incluses.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.interdisciplinary.incluses.models.Message;
import project.interdisciplinary.incluses.models.Setor;
import project.interdisciplinary.incluses.models.dto.CriarSetorDTO;
import project.interdisciplinary.incluses.services.SetorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/setor")
public class SetorController {
    private final SetorService setorService;

    @Autowired
    public SetorController(SetorService setorService) {
        this.setorService = setorService;
    }

    @Operation(summary = "Listar todos os setores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de setores retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/selecionar")
    public List<Setor> listarSetores() {
        return setorService.listarSetores();
    }

    @Operation(summary = "Inserir um novo setor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setor inserido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no setor"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/inserir")
    public ResponseEntity<Object> inserirSetor(@Valid @RequestBody CriarSetorDTO setor, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            setorService.criarSetor(setor);
            Map<String, String> response = new HashMap<>();
            response.put("message","ok");
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Excluir um setor pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setor excluído com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "404", description = "Setor não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Object> excluirSetor(@PathVariable UUID id) {
        if (setorService.excluirSetor(id) == true) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ok");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar um setor pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setor atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação no setor"),
            @ApiResponse(responseCode = "404", description = "Setor não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarSetor(@PathVariable UUID id, @Valid @RequestBody Setor setorAtualizado, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : resultado.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Setor setor = setorService.buscarSetorPorId(id);
            setor.setNome(setorAtualizado.getNome());
            setorService.salvarSetor(setor);
            Map<String, String> response = new HashMap<>();
            response.put("message","ok");
            return ResponseEntity.ok(response);
        }
    }
}
