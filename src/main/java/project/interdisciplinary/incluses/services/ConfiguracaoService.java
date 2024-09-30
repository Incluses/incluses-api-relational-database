package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Configuracao;
import project.interdisciplinary.incluses.repositories.ConfiguracaoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfiguracaoService {
    private final ConfiguracaoRepository configuracaoRepository;
    public ConfiguracaoService(ConfiguracaoRepository configuracaoRepository) {
        this.configuracaoRepository = configuracaoRepository;
    }
    public List<Configuracao> listarConfiguracoes() {
        return configuracaoRepository.findAll();
    }

    public Configuracao salvarConfiguracao(Configuracao configuracao){
        return configuracaoRepository.save(configuracao);
    }

    public Configuracao buscarConfiguracaoPorId(UUID id){
        return configuracaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Configuracao não encontrado"));
    }
    public Configuracao excluirConfiguracao(UUID id){
        Optional<Configuracao> conf = configuracaoRepository.findById(id);
        if(conf.isPresent()){
            configuracaoRepository.deleteById(id);
            return conf.get();
        }
        return null;
    }
}
