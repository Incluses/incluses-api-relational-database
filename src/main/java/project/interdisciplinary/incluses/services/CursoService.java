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
        return cursoRepository.findAllPermissao();
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
        Optional<List<Curso>> cursos = cursoRepository.findCursosByNomeContainsIgnoreCase(nome.toLowerCase());
        if (cursos.isPresent()){
            return cursos.get();
        }
        else {
            return null;
        }
    }
    public List<Curso> findByFkPerfil(UUID fkPerfil){
        Optional<List<Curso>> cursos = cursoRepository.findCursosByFkPerfilId(fkPerfil);
        if (cursos.isPresent()){
            return cursos.get();
        }
        else {
            return null;
        }
    }

    public UUID criarCurso(CriarCursoDTO criarCursoDTO){
        return cursoRepository.criarCurso(criarCursoDTO.getDescricao(), criarCursoDTO.getNome(), criarCursoDTO.getPerfilId());
    }
}
