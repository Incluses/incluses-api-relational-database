package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Endereco;
import project.interdisciplinary.incluses.repositories.EnderecoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }
    public List<Endereco> listarEnderecos() {
        return enderecoRepository.findAll();
    }

    public Endereco salvarEndereco(Endereco endereco){
        return enderecoRepository.save(endereco);
    }

    public Endereco buscarEnderecoPorId(UUID id){
        return enderecoRepository.findById(id).orElseThrow(() -> new RuntimeException("Endereco não encontrado"));
    }
    public Endereco excluirEndereco(UUID id){
        Optional<Endereco> ende = enderecoRepository.findById(id);
        if(ende.isPresent()){
            enderecoRepository.deleteById(id);
            return ende.get();
        }
        return null;
    }
}
