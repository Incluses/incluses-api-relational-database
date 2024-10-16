package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.InscricaoCurso;
import project.interdisciplinary.incluses.models.Vaga;
import project.interdisciplinary.incluses.models.dto.CriarInscricaoCursoDTO;
import project.interdisciplinary.incluses.models.dto.CriarInscricaoVagaDTO;
import project.interdisciplinary.incluses.repositories.InscricaoCursoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InscricaoCursoService {
    private final InscricaoCursoRepository inscricaoCursoRepository;
    public InscricaoCursoService(InscricaoCursoRepository inscricaoCursoRepository) {
        this.inscricaoCursoRepository = inscricaoCursoRepository;
    }
    public List<InscricaoCurso> listarInscricoesCursos() {
        return inscricaoCursoRepository.findAll();
    }

    public InscricaoCurso salvarInscricaoCurso(InscricaoCurso inscricaoCurso){
        return inscricaoCursoRepository.save(inscricaoCurso);
    }

    public InscricaoCurso buscarInscricaoCursoPorId(UUID id){
        return inscricaoCursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Configuracao não encontrado"));
    }
    public InscricaoCurso excluirInscricaoCurso(UUID id){
        Optional<InscricaoCurso> insc = inscricaoCursoRepository.findById(id);
        if(insc.isPresent()){
            inscricaoCursoRepository.deleteById(id);
            return insc.get();
        }
        return null;
    }
    public boolean criarInscricaoCurso(CriarInscricaoCursoDTO criarInscricaoCursoDTO){
        if(validateInscricao(criarInscricaoCursoDTO.getCursoId(), criarInscricaoCursoDTO.getUsuarioId())){
            inscricaoCursoRepository.criarInscricaoCurso(criarInscricaoCursoDTO.getUsuarioId(), criarInscricaoCursoDTO.getCursoId());
            return true;
        }
        else {
            return false;
        }
    }
    public boolean validateInscricao(UUID fkCurso, UUID fkUsuario){
        Optional<InscricaoCurso> inscricaoCurso = inscricaoCursoRepository.findInscricaoExistente(fkCurso, fkUsuario);
        if (inscricaoCurso.isPresent()){
            return false;
        }
        else {
            return true;
        }
    }
    public List<InscricaoCurso> findInscricaoByFkUsuario(UUID fkUsuario){
        Optional<List<InscricaoCurso>> inscricaoCurso = inscricaoCursoRepository.findInscricaoCursosByFkUsuarioId(fkUsuario);
        if (inscricaoCurso.isPresent()){
            return inscricaoCurso.get();
        }
        else {
            return null;
        }
    }
}
