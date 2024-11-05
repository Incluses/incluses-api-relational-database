package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.models.TipoArquivo;
import project.interdisciplinary.incluses.repositories.TipoArquivoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TipoArquivoService {
    private final TipoArquivoRepository tipoArquivoRepository;
    public TipoArquivoService(TipoArquivoRepository tipoArquivoRepository) {
        this.tipoArquivoRepository = tipoArquivoRepository;
    }
    public List<TipoArquivo> listarTipoArquivos() {
        return tipoArquivoRepository.findAll();
    }

    public TipoArquivo salvarTipoArquivo(TipoArquivo tipoArquivo){
        return tipoArquivoRepository.save(tipoArquivo);
    }

    public TipoArquivo buscarTipoArquivoPorId(UUID id){
        return tipoArquivoRepository.findById(id).orElseThrow(() -> new RuntimeException("TipoArquivo não encontrado"));
    }
    public TipoArquivo findByNome(String nome){
        Optional<TipoArquivo> tipo = tipoArquivoRepository.findByNomeIgnoreCase(nome);
        if (tipo.isPresent()){
            return tipo.get();
        }
        else {
            return null;
        }
    }
}
