package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.TipoVaga;
import project.interdisciplinary.incluses.repositories.TipoVagaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TipoVagaService {
    private final TipoVagaRepository tipoVagaRepository;
    public TipoVagaService(TipoVagaRepository tipoVagaRepository) {
        this.tipoVagaRepository = tipoVagaRepository;
    }
    public List<TipoVaga> listarTiposVaga() {
        return tipoVagaRepository.findAll();
    }

    public TipoVaga salvarTipoVaga(TipoVaga tipoVaga){
        return tipoVagaRepository.save(tipoVaga);
    }

    public TipoVaga buscarTipoVagaPorId(UUID id){
        return tipoVagaRepository.findById(id).orElseThrow(() -> new RuntimeException("TipoVaga não encontrado"));
    }
}
