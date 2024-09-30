package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.SituacaoTrabalhista;
import project.interdisciplinary.incluses.repositories.SituacaoTrabalhistaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SituacaoTrabalhistaService {
    private final SituacaoTrabalhistaRepository situacaoTrabalhistaRepository;

    public  SituacaoTrabalhistaService(SituacaoTrabalhistaRepository situacaoTrabalhistaRepository) {
        this.situacaoTrabalhistaRepository = situacaoTrabalhistaRepository;
    }

    public List<SituacaoTrabalhista> listarSituacoesTrabalhistas() {
        return situacaoTrabalhistaRepository.findAll();
    }

    public SituacaoTrabalhista salvarSituacaoTrabalhista (SituacaoTrabalhista situacaoTrabalhista) {
        return situacaoTrabalhistaRepository.save(situacaoTrabalhista);
    }

    public SituacaoTrabalhista buscarSituacaoTrabalhistaPorId(UUID id) {
        return situacaoTrabalhistaRepository.findById(id).orElseThrow(() -> new RuntimeException("SituacaoTrabalhis não encontrado"));
    }

    public SituacaoTrabalhista excluirSituacaoTrabalhista(UUID id) {
        Optional<SituacaoTrabalhista> situ = situacaoTrabalhistaRepository.findById(id);
        if (situ.isPresent()) {
            situacaoTrabalhistaRepository.deleteById(id);
            return situ.get();
        }
        return null;
    }
}
