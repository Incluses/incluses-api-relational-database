package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.TipoPerfil;
import project.interdisciplinary.incluses.repositories.TipoPerfilRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TipoPerfilService {
    private final TipoPerfilRepository tipoPerfilRepository;
    public TipoPerfilService(TipoPerfilRepository tipoPerfilRepository) {
        this.tipoPerfilRepository = tipoPerfilRepository;
    }
    public List<TipoPerfil> listarTiposPerfis() {
        return tipoPerfilRepository.findAll();
    }

    public TipoPerfil salvarTipoPerfil(TipoPerfil tipoPerfil){
        return tipoPerfilRepository.save(tipoPerfil);
    }

    public TipoPerfil buscarTipoPerfilPorId(UUID id){
        return tipoPerfilRepository.findById(id).orElseThrow(() -> new RuntimeException("TipoPerfil não encontrado"));
    }
}
