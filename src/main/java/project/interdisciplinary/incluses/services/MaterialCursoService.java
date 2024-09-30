package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.MaterialCurso;
import project.interdisciplinary.incluses.repositories.MaterialCursoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MaterialCursoService {
    private final MaterialCursoRepository materialCursoRepository;
    public MaterialCursoService(MaterialCursoRepository materialCursoRepository) {
        this.materialCursoRepository = materialCursoRepository;
    }
    public List<MaterialCurso> listarMateriaisCursos() {
        return materialCursoRepository.findAll();
    }

    public MaterialCurso salvarMaterialCurso(MaterialCurso materialCurso){
        return materialCursoRepository.save(materialCurso);
    }

    public MaterialCurso buscarMaterialCursoPorId(UUID id){
        return materialCursoRepository.findById(id).orElseThrow(() -> new RuntimeException("MaterialCurso não encontrado"));
    }
    public MaterialCurso excluirMaterialCurso(UUID id){
        Optional<MaterialCurso> mate = materialCursoRepository.findById(id);
        if(mate.isPresent()){
            materialCursoRepository.deleteById(id);
            return mate.get();
        }
        return null;
    }
}
