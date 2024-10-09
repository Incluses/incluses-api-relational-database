package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Arquivo;
import project.interdisciplinary.incluses.models.PermissaoCurso;
import project.interdisciplinary.incluses.repositories.ArquivoRepository;
import project.interdisciplinary.incluses.repositories.PermissaoCursoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PermissaoCursoService {
    private final PermissaoCursoRepository permissaoCursoRepository;
    public PermissaoCursoService(PermissaoCursoRepository permissaoCursoRepository) {
        this.permissaoCursoRepository = permissaoCursoRepository;
    }
    public List<PermissaoCurso> listarPermissoesCursos() {
        return permissaoCursoRepository.findAll();
    }

    public PermissaoCurso salvarPermissaoCurso(PermissaoCurso permissaoCurso){
        return permissaoCursoRepository.save(permissaoCurso);
    }

    public PermissaoCurso buscarPermissaoPorId(UUID id){
        return permissaoCursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Permissao curso não encontrado"));
    }
    public PermissaoCurso excluirPermissaoCurso(UUID id){
        Optional<PermissaoCurso> perm = permissaoCursoRepository.findById(id);
        if(perm.isPresent()){
            permissaoCursoRepository.deleteById(id);
            return perm.get();
        }
        return null;
    }
}
