package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Setor;
import project.interdisciplinary.incluses.repositories.SetorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SetorService {
    private final SetorRepository setorRepository;

    public  SetorService(SetorRepository setorRepository) {
        this.setorRepository = setorRepository;
    }

    public List<Setor> listarSetores() {
        return setorRepository.findAll();
    }

    public Setor salvarSetor (Setor setor) {
        return setorRepository.save(setor);
    }

    public Setor buscarSetorPorId(UUID id) {
        return setorRepository.findById(id).orElseThrow(() -> new RuntimeException("Setor não encontrado"));
    }

    public Setor excluirSetor(UUID id) {
        Optional<Setor> setr = setorRepository.findById(id);
        if (setr.isPresent()) {
            setorRepository.deleteById(id);
            return setr.get();
        }
        return null;
    }
}