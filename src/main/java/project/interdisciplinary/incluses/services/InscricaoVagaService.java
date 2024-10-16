package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.InscricaoCurso;
import project.interdisciplinary.incluses.models.InscricaoVaga;
import project.interdisciplinary.incluses.models.dto.CriarInscricaoVagaDTO;
import project.interdisciplinary.incluses.repositories.InscricaoCursoRepository;
import project.interdisciplinary.incluses.repositories.InscricaoVagaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InscricaoVagaService {
    private final InscricaoVagaRepository inscricaoVagaRepository;
    public InscricaoVagaService(InscricaoVagaRepository inscricaoVagaRepository) {
        this.inscricaoVagaRepository = inscricaoVagaRepository;
    }
    public List<InscricaoVaga> listarInscricaoVaga() {
        return inscricaoVagaRepository.findAll();
    }

    public InscricaoVaga salvarInscricaoVaga(InscricaoVaga inscricaoVaga){
        return inscricaoVagaRepository.save(inscricaoVaga);
    }

    public InscricaoVaga buscarInscricaoVagaPorId(UUID id){
        return inscricaoVagaRepository.findById(id).orElseThrow(() -> new RuntimeException("Configuracao não encontrado"));
    }
    public InscricaoVaga excluirInscricaoVaga(UUID id){
        Optional<InscricaoVaga> insc = inscricaoVagaRepository.findById(id);
        if(insc.isPresent()){
            inscricaoVagaRepository.deleteById(id);
            return insc.get();
        }
        return null;
    }
    public boolean criarInscricaoVaga(CriarInscricaoVagaDTO criarInscricaoVagaDTO){
        if(validateInscricao(criarInscricaoVagaDTO.getVagaId(), criarInscricaoVagaDTO.getUsuarioId())){
            inscricaoVagaRepository.criarInscricaoVaga(criarInscricaoVagaDTO.getUsuarioId(), criarInscricaoVagaDTO.getVagaId());
            return true;
        }
        else {
            return false;
        }
    }

    public boolean validateInscricao(UUID fkVaga, UUID fkUsuario){
        Optional<InscricaoVaga> inscricaoVaga = inscricaoVagaRepository.findInscricaoExistente(fkVaga, fkUsuario);
        if (inscricaoVaga.isPresent()){
            return false;
        }
        else {
            return true;
        }
    }
    public List<InscricaoVaga> findInscricaoByFkUsuario(UUID fkUsuario){
        Optional<List<InscricaoVaga>> inscricaoVaga = inscricaoVagaRepository.findInscricaoVagasByFkUsuarioId(fkUsuario);
        if (inscricaoVaga.isPresent()){
            return inscricaoVaga.get();
        }
        else {
            return null;
        }
    }
}
