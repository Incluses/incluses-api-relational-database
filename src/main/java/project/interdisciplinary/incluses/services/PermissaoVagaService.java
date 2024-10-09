package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Arquivo;
import project.interdisciplinary.incluses.models.PermissaoVaga;
import project.interdisciplinary.incluses.repositories.ArquivoRepository;
import project.interdisciplinary.incluses.repositories.PermissaoVagaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PermissaoVagaService {

    private final PermissaoVagaRepository permissaoVagaRepository;
    public PermissaoVagaService(PermissaoVagaRepository permissaoVagaRepository) {
        this.permissaoVagaRepository = permissaoVagaRepository;
    }
    public List<PermissaoVaga> listarPermissaoVaga() {
        return permissaoVagaRepository.findAll();
    }

    public PermissaoVaga salvarPermissaoVaga(PermissaoVaga permissaoVaga){
        return permissaoVagaRepository.save(permissaoVaga);
    }

    public PermissaoVaga buscarPermissaoVagaPorId(UUID id){
        return permissaoVagaRepository.findById(id).orElseThrow(() -> new RuntimeException("Permissao vaga não encontrado"));
    }
    public PermissaoVaga excluirPermissaoVaga(UUID id){
        Optional<PermissaoVaga> perm = permissaoVagaRepository.findById(id);
        if(perm.isPresent()){
            permissaoVagaRepository.deleteById(id);
            return perm.get();
        }
        return null;
    }
}
