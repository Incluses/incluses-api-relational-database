package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Curso;
import project.interdisciplinary.incluses.models.Vaga;
import project.interdisciplinary.incluses.models.dto.CriarCursoDTO;
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
    public boolean excluirCurso(UUID id){
        UUID[] ids = new UUID[100];
        ids[0] = id;
        cursoRepository.deletarCurso(ids);
        Optional<Curso> curs = cursoRepository.findById(id);
        if(curs.isPresent()){
            return false;
        }
        else {
            return true;
        }
    }
    public List<Curso> findByNome(String nome){
        Optional<List<Curso>> cursos = cursoRepository.findCursosByNomeContains(nome);
        if (cursos.isPresent()){
            return cursos.get();
        }
        else {
            return null;
        }
    }

    public void criarCurso(CriarCursoDTO criarCursoDTO){
        cursoRepository.criarCurso(criarCursoDTO.getDescricao(), criarCursoDTO.getNome(),
                criarCursoDTO.getPerfilId());
    }
}
