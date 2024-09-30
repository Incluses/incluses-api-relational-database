package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.repositories.CursoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CursoService {
    private final CursoRepository cursoRepository;
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository  = cursoRepository;
    }
    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso salvarCurso(Curso curso){
        return cursoRepository.save(curso);
    }

    public Curso buscarCursoPorId(UUID id){
        return cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Curso não encontrado"));
    }
    public Curso excluirCurso(UUID id){
        Optional<Curso> curs = cursoRepository.findById(id);
        if(curs.isPresent()){
            cursoRepository.deleteById(id);
            return curs.get();
        }
        return null;
    }
}
