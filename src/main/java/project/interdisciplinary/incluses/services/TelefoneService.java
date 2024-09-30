package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Telefone;
import project.interdisciplinary.incluses.repositories.TelefoneRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TelefoneService {
    private final TelefoneRepository telefoneRepository;
    public TelefoneService(TelefoneRepository telefoneRepository) {
        this.telefoneRepository  = telefoneRepository;
    }
    public List<Telefone> listarTelefones() {
        return telefoneRepository.findAll();
    }

    public Telefone salvarTelefone(Telefone telefone){
        return telefoneRepository.save(telefone);
    }

    public Telefone buscarTelefonePorId(UUID id){
        return telefoneRepository.findById(id).orElseThrow(() -> new RuntimeException("Telefone não encontrado"));
    }
    public Telefone excluirTelefone(UUID id){
        Optional<Telefone> tele = telefoneRepository.findById(id);
        if(tele.isPresent()){
            telefoneRepository.deleteById(id);
            return tele.get();
        }
        return null;
    }
}
