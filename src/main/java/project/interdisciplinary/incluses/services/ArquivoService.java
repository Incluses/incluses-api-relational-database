package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Arquivo;
import project.interdisciplinary.incluses.repositories.ArquivoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArquivoService {
    private final ArquivoRepository arquivoRepository;
    public ArquivoService(ArquivoRepository arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
    }
    public List<Arquivo> listarArquivos() {
        return arquivoRepository.findAll();
    }

    public Arquivo salvarArquivo(Arquivo arquivo){
        return arquivoRepository.save(arquivo);
    }

    public Arquivo buscarArquivoPorId(UUID id){
        return arquivoRepository.findById(id).orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));
    }
    public Arquivo excluirArquivo(UUID id){
        Optional<Arquivo> arqv = arquivoRepository.findById(id);
        if(arqv.isPresent()){
            arquivoRepository.deleteById(id);
            return arqv.get();
        }
        return null;
    }
}
