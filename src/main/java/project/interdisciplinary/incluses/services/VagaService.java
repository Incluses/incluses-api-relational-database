package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Vaga;
import project.interdisciplinary.incluses.repositories.VagaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VagaService {
    private final VagaRepository vagaRepository;
    public VagaService(VagaRepository vagaRepository) {
        this.vagaRepository = vagaRepository;
    }
    public List<Vaga> listarVagas() {
        return vagaRepository.findAll();
    }

    public Vaga salvarVaga(Vaga Vaga){
        return vagaRepository.save(Vaga);
    }

    public Vaga buscarVagaPorId(UUID id){
        return vagaRepository.findById(id).orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
    }
    public Vaga excluirVaga(UUID id){
        Optional<Vaga> vaga = vagaRepository.findById(id);
        if(vaga.isPresent()){
            vagaRepository.deleteById(id);
            return vaga.get();
        }
        return null;
    }
}
