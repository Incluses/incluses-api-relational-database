package project.interdisciplinary.incluses.services;

import org.springframework.stereotype.Service;
import project.interdisciplinary.incluses.models.Usuario;
import project.interdisciplinary.incluses.models.dto.CriarUsuarioDTO;
import project.interdisciplinary.incluses.repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario acharPorFkPerfil(UUID fkPerfil){
        Optional<Usuario> usuario = usuarioRepository.findUserByFkPerfil(fkPerfil);
        if(usuario.isPresent()){
            return usuario.get();
        }
        else {
            return null;
        }
    }

    public void registrarUsuario(CriarUsuarioDTO criarUsuarioDTO){
        usuarioRepository.criarUsuario(criarUsuarioDTO.getCpf(),criarUsuarioDTO.getDtNascimento(),criarUsuarioDTO.getPronomes(),
                criarUsuarioDTO.getNomeSocial(), criarUsuarioDTO.getNome(), criarUsuarioDTO.getSenha(),
                criarUsuarioDTO.getEmail(), criarUsuarioDTO.getTelefone());
    }
    public Usuario salvarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarUsuarioPorId(UUID id){
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
    }
    public boolean excluirUsuario(UUID id){
       usuarioRepository.deletarUsuario(id);
       Optional<Usuario> deletado = usuarioRepository.findById(id);
       if(deletado.isPresent()){
           return false;
       }
       else {
           return true;
       }
    }
}
