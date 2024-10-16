package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.InscricaoVaga;
import project.interdisciplinary.incluses.models.MaterialCurso;
import project.interdisciplinary.incluses.models.dto.CriarMaterialCursoDTO;
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
    public boolean excluirMaterialCurso(UUID id){
        materialCursoRepository.deleteMaterialCurso(id);
        Optional<MaterialCurso> mate = materialCursoRepository.findById(id);
        if(mate.isPresent()){
            return false;
        }
        else {
            return true;
        }
    }

    public void criarMaterialCurso(CriarMaterialCursoDTO criarMaterialCursoDTO){
        materialCursoRepository.criarMaterialCurso(criarMaterialCursoDTO.getDescricao(),
                criarMaterialCursoDTO.getCursoId(),criarMaterialCursoDTO.getArquivoId(), criarMaterialCursoDTO.getNome());
    }

    public List<MaterialCurso> findMaterialByFkCurso(UUID fkCurso){
        Optional<List<MaterialCurso>> materialCursos = materialCursoRepository.findMaterialCursosByFkCursoId(fkCurso);
        if (materialCursos.isPresent()){
            return materialCursos.get();
        }
        else {
            return null;
        }
    }
}
