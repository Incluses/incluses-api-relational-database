package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.models.Perfil;
import project.interdisciplinary.incluses.models.Vaga;
import project.interdisciplinary.incluses.models.dto.CriarVagaDTO;
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
    public boolean excluirVaga(UUID id){
        UUID[] ids = new UUID[100];
        ids[0] = id;
        vagaRepository.deletarVaga(ids);
        Optional<Vaga> vaga = vagaRepository.findById(id);
        if(vaga.isPresent()){
            return false;
        }
        else {
            return true;
        }
    }
    public List<Vaga> findByNome(String nome){
        Optional<List<Vaga>> vagas = vagaRepository.findVagasByNomeContains(nome);
        if (vagas.isPresent()){
            return vagas.get();
        }
        else {
            return null;
        }
    }
    public void criarVaga(CriarVagaDTO criarVagaDTO){
        vagaRepository.criarVaga(criarVagaDTO.getDescricao(), criarVagaDTO.getNome(), criarVagaDTO.getEmpresaId(),criarVagaDTO.getTipoVagaId());
    }
}
