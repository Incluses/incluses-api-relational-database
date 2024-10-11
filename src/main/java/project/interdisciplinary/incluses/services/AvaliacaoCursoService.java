package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Arquivo;
import project.interdisciplinary.incluses.models.AvaliacaoCurso;
import project.interdisciplinary.incluses.models.Usuario;
import project.interdisciplinary.incluses.repositories.ArquivoRepository;
import project.interdisciplinary.incluses.repositories.AvaliacaoCursoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AvaliacaoCursoService {
    private final AvaliacaoCursoRepository avaliacaoCursoRepository;
    public AvaliacaoCursoService(AvaliacaoCursoRepository avaliacaoCursoRepository) {
        this.avaliacaoCursoRepository = avaliacaoCursoRepository;
    }
    public List<AvaliacaoCurso> listarAvaliacoes() {
        return avaliacaoCursoRepository.findAll();
    }

    public List<AvaliacaoCurso> acharPorFkUser(UUID fkUser){
        Optional<List<AvaliacaoCurso>> avaliacaoCursos = avaliacaoCursoRepository.findAvaliacaoCursosByFkUsuario(fkUser);
        if(avaliacaoCursos.isPresent()){
            return avaliacaoCursos.get();
        }
        else {
            return null;
        }
    }
    public AvaliacaoCurso salvarAvaliacao(AvaliacaoCurso avaliacaoCurso){
        return avaliacaoCursoRepository.save(avaliacaoCurso);
    }

    public AvaliacaoCurso buscarAvaliacaoPorId(UUID id){
        return avaliacaoCursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
    }
    public AvaliacaoCurso excluirAvaliacao(UUID id){
        Optional<AvaliacaoCurso> aval = avaliacaoCursoRepository.findById(id);
        if(aval.isPresent()){
            avaliacaoCursoRepository.deleteById(id);
            return aval.get();
        }
        return null;
    }
}
